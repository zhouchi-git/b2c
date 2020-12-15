package com.mr.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mr.mapper.BrandMapper;
import com.mr.mapper.CategoryBrandMapper;
import com.mr.pojo.Brand;
import com.mr.pojo.Category_Brand;
import com.mr.utils.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    /**
     *
     * 查询
     * @param
     * @return
     */
    public PageResult brandList(Integer page,Integer rows,String sortBy,Boolean desc,String key) {
        //开启分页
        PageHelper.startPage(page,rows);//有疑问
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        //name模糊查询
        if(key != null && key !=""){
            criteria.andLike("name","%"+key+"%").orEqualTo("letter",key.toUpperCase());
        }
        //排序
        example.setOrderByClause(sortBy+"   "+(desc?"DESC":"ASC"));
        Page<Brand> brands = (Page<Brand>) brandMapper.selectByExample(example);//也有疑问
        return new PageResult(brands.getTotal(),brands);
    }

    /**
     * 新增与修改
     * @param brand
     * @param categories
     */
    @Transactional
    public void addOrBrand(Brand brand, String categories) {
        if(categories != null || categories.length()!= 0){
            Category_Brand category_brand = null;
            String[] split = categories.split(",");
            if(brand.getId() != null){
                Example example = new Example(Category_Brand.class);
                example.createCriteria().andEqualTo("brandId",brand.getId());
                categoryBrandMapper.deleteByExample(example);
                brandMapper.updateByPrimaryKeySelective(brand);
            }else{
                brandMapper.insertSelective(brand);
            }

            //System.out.println(brand.getId());
            category_brand = new Category_Brand();
            category_brand.setBrandId(brand.getId());
            //通过id与分类id新增中间表数据
            for (int i = 0; i < split.length; i++) {
                category_brand.setCategoryId(Long.parseLong(split[i]));
                categoryBrandMapper.insert(category_brand);
            }
        }
    }

    /**
     * 删除
     * @param bid
     */
    @Transactional
    public void delBrand(Long bid) {
        //删除品牌信息
        brandMapper.deleteByBrandId(bid);
        //删除中间表数据
        brandMapper.delCategory_BrandByBrandId(bid);
    }

    /**
     * 根据分类id 获取分类下的所有品牌
     * @param cid
     * @return
     */
    @Transactional
    public List<Brand> getBrandById(Long cid) {
        //cid  通过它 查询到分类与品牌中间表的所有的品牌id
        List<Long> category_brands = categoryBrandMapper.selectByCategory_BrandByCategoryId(cid);
        //通过in 品牌id 查询出所有的品牌
        return brandMapper.selectBrandByIds(StringUtils.join(category_brands,","));
    }

    /**
     * 根据id  查询品牌信息
     * @param bid
     * @return
     */
    public Brand getBrandByBid(Long bid) {
        return  brandMapper.selectByPrimaryKey(bid);
    }
}
