package com.mr.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mr.mapper.*;
import com.mr.pojo.*;
import com.mr.utils.MqMessageConstant;
import com.mr.utils.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private  StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;
    /**
     * 商品展示
     * @param
     * @return
     */
    public PageResult<Spu> spuList(Integer page,Integer rows,String sortBy,Boolean desc,String key,Boolean saleable) {
        //开启分分页
        PageHelper.startPage(page,rows);
        //条件查询
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //过滤上下架
        if(saleable!= null){
            criteria.andEqualTo("saleable",saleable);
        }
        //模糊查询 title
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        //排序
        example.setOrderByClause(sortBy+"  "+(desc?"DESC":"ASC"));
        //查询分页数据
        Page<Spu> spuPage = (Page<Spu>)spuMapper.selectByExample(example);
        //循环分页数据完成业务
        List<Spu> spuList = spuPage.getResult().stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu,spuBo);
            //创建集合放分类
            List<Long> longs = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
            //通过分类id查询分类名
            List<String> names = categoryMapper.selectCatGoryNameByIds(StringUtils.join(longs, ","));
            //返回中文分类
            spuBo.setCategoryName(StringUtils.join(names, "/"));
            //查询品牌
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBrandName(brand.getName());
            return spuBo;
        }).collect(Collectors.toList());
        return  new PageResult<Spu>(spuPage.getTotal(),spuList);
    }

    /**
     * 商品新增与修改
     * @param spubo
     */
    @Transactional
    public void addOrUpdateGoods(SpuBo spubo) {
        if( !spubo.getSkus().isEmpty() && spubo.getSkus().size()>0 && spubo.getSpuDetail()!=null){
            Spu spu=null;
            if(spubo.getId()==null){
                //新增spu
                spu = new Spu(null, spubo.getBrandId(), spubo.getCid1(), spubo.getCid2(), spubo.getCid3(), spubo.getTitle(), spubo.getSubTitle(), true, true, new Date(), new Date());
                spuMapper.insertSelective(spu);
            }else {
                //修改spu
                spu = new Spu(spubo.getId(), spubo.getBrandId(), spubo.getCid1(), spubo.getCid2(), spubo.getCid3(), spubo.getTitle(), spubo.getSubTitle(), true, true, null, new Date());
                spuMapper.updateByPrimaryKeySelective(spu);
            }
            //修改suk 如果id存在就修改 不存在就添加
            for (Sku sku : spubo.getSkus()) {
                    sku.setLastUpdateTime(new Date());
                    sku.setSpuId(spu.getId());
                    if(sku.getId() == null){
                        //新增添加创建时间
                        sku.setCreateTime(new Date());
                        //新增sku
                        skuMapper.insertSelective(sku);
                        //新增库存
                        stockMapper.insertSelective(new Stock(sku.getId(),null,null,sku.getStock()));
                    }else{
                        //创建时间不变
                        sku.setCreateTime(null);
                        //更新修改时间
                        sku.setLastUpdateTime(new Date());
                        //修改sku
                        skuMapper.updateByPrimaryKeySelective(sku);
                        //修改库存
                        stockMapper.updateByPrimaryKeySelective(new Stock(sku.getId(),null,null,sku.getStock()));
                    }
            }

            SpuDetail spuDetail = spubo.getSpuDetail();
            if(spuDetail.getSpuId() == null){
                //新增商品详情
                spuDetail.setSpuId(spu.getId());
                spuDetailMapper.insertSelective(spuDetail);
                //新增完商品后向队列发送消息 内容 商品 id  执行的什么方法
                amqpTemplate.convertAndSend(MqMessageConstant.SPU_EXCHANGE_NAME,MqMessageConstant.SPU_ROUT_KEY_SAVE,spu.getId());
            }else{
                //修改商品详情
                spuDetailMapper.updateByPrimaryKeySelective(spuDetail);
                amqpTemplate.convertAndSend(MqMessageConstant.SPU_EXCHANGE_NAME,MqMessageConstant.SPU_ROUT_KEY_UPDATE,spu.getId());
            }
        }
    }

    /**
     * 商品下架
     * @param id
     */
    public void deleteGoods(Long id) {
        spuMapper.deleteGoods(id);
        amqpTemplate.convertAndSend(MqMessageConstant.SPU_EXCHANGE_NAME,MqMessageConstant.SPU_ROUT_KEY_DELETE,id);
    }

    /**
     * 查询商品详情
     * @param spu_id
     * @return
     */
    public SpuDetail getDetail(Long spu_id) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spu_id);
        return  spuDetail;
    }

    /**
     * 根据spu_id查询skus
     * @param spu_id
     * @return
     */
    public List<Sku> getSkus(Long spu_id) {

       return skuMapper.selectSkusBySpuId(spu_id);
    }

    /**
     * 商品的上架
     * @param id
     */
    public void showGoods(Long id) {
        spuMapper.showGoods(id);
        amqpTemplate.convertAndSend(MqMessageConstant.SPU_EXCHANGE_NAME,MqMessageConstant.SPU_QUEUE_SEARCH_DELETE,id);
    }

    /**
     * 通过id 查询spu
     * @param id
     * @return
     */
    public Spu getSpuById(Long id) {
        return  spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据spu_id  查询商品详情
     * @param spuId
     * @return
     */
    public SpuDetail getSpuDetailBySpuId(Long spuId) {

        return  spuDetailMapper.selectByPrimaryKey(spuId);
    }

    /**
     * 根据spu_id查询skus(单表)
     * @param spu_id
     * @return
     */
    public List<Sku> getSkuBySpuId_Dan(Long spu_id) {
        Sku sku = new Sku();
        sku.setSpuId(spu_id);
        return  skuMapper.select(sku);
    }
    /**
     * 根据商品id 查询库存
     * @param skuId
     * @return
     */
    public Stock getStockBySkuId(Long skuId) {
        return  stockMapper.selectByPrimaryKey(skuId);
    }

    /**
     * 根据id查询sku
     * @param id
     * @return
     */
    public Sku getSkuById(Long id) {
        Sku sku = skuMapper.selectByPrimaryKey(id);
        Stock stock = stockMapper.selectByPrimaryKey(sku.getId());
        sku.setStock(stock.getStock());
        return  sku;
    }

    /**
     * 查询所有的sku
     * @return
     */
    public List<Sku> queryListSku() {
        return  skuMapper.selectAll().stream().map(sku -> {
            sku.setStock(stockMapper.selectByPrimaryKey(sku.getId()).getStock());
            return sku;
        }).collect(Collectors.toList());

    }
    /**
     *根据skuId 修改stock库存
     * @param skuId
     * @param num
     * @return
     */
    public int updateStockBySKuId(Long skuId, Integer num) {
        return  stockMapper.updateStockBySKuId(skuId,num);
    }
}



