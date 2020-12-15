package com.mr.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mr.bo.PageParam;
import com.mr.client.*;
import com.mr.dao.GoodRepository;
import com.mr.pojo.*;
import com.mr.utils.HighLightUtil;
import com.mr.utils.JsonUtils;
import com.mr.utils.PageResult;
import com.mr.utils.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GoodsService {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private SpecClient specClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private GroupClient groupClient;
    @Autowired
    private GoodRepository goodRepository;
    @Autowired
    private ElasticsearchTemplate template;

    /**
     * 批量往es中存放数据
     * @param id
     * @return
     */
    public Goods getGoods(Long id){
        Spu spu = goodsClient.getSpuById(id);
        // 所有需要被搜索的信息，包含标题，分类，甚至品牌
        Brand brand = brandClient.getBrandByBid(spu.getBrandId());
        List<String> categoryNames = categoryClient.getCategoryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        // sku信息的json结构
        List<Sku> skus = goodsClient.getSkus(spu.getId());
        //价格
        List<Long> prices = new  ArrayList<>();
        List<Map<String, Object>> skuList = new ArrayList<>();
        skus.forEach(sku -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("image",StringUtils.isEmpty(sku.getImages())?"" : sku.getImages().split(",")[0]);
            map.put("price",sku.getPrice());
            prices.add(sku.getPrice());
            skuList.add(map);
        });

        // 查询出规格参数
        List<SpecParam> specParam = specClient.getParamsByCateGroupId(spu.getCid3(), null, true,null);
        //查询出商品详情
        SpuDetail spuDetail = goodsClient.getSpuDetailBySpuId(spu.getId());
        //将通用属性转换为Map
        Map<Long, String> genericMap = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);
        //将特殊属性转换为Map
       Map<Long,List<String>> specialMap=JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>(){});
        //创建map 用于存放规格参数
       Map<String, Object>  detailMap = new HashMap<>();
       //循环遍历查询出来的规格参数
        specParam.forEach(param -> {
            //key值为规格参数的name
            String key = param.getName();
            //value值为通用属性与特有属性的值
            Object value = null;
            //判断是否为通用属性true为通用属性
            if(param.getGeneric()){
                //通过规格的id获取通用属性的值
                value = genericMap.get(param.getId());
                //判断通用属性是否为数值类型
                if(param.getNumeric()){
                    //如果是数值类型就设置区间
                   value = this.chooseSegment(value.toString(),param);
                }
            }else{
                //特殊属性赋值
                value = specialMap.get(param.getId());
            }
            //存放在详情map中
            detailMap.put(key, value);
        });



        Goods goods = new Goods();
        goods.setId(spu.getId());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        goods.setAll(spu.getTitle()+" "+brand.getName()+" "+ StringUtils.join(categoryNames,","));
        goods.setPrice(prices);
        goods.setSkus(JSON.toJSONString(skuList));
        goods.setSpecs(detailMap);
        return goods;
    }

    /**
     * 拼接数值类型
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    /**
     * es查询数据
     * @param pageParam
     * @return
     */
    public PageResult<Goods> queryGoodsPage(PageParam pageParam) {
        //创建查询对象
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(pageParam != null && StringUtils.isNotEmpty(pageParam.getKey())){
            //设置查询参数
            boolQueryBuilder.must(QueryBuilders.matchQuery("all",pageParam.getKey()));
        }
        //分页
        queryBuilder.withPageable(PageRequest.of(pageParam.getPage()-1, pageParam.getRows()));
        //聚合分组查询分类
        queryBuilder.addAggregation(AggregationBuilders.terms("category").field("cid3"));
        //聚合分组查询品牌
        queryBuilder.addAggregation(AggregationBuilders.terms("brand").field("brandId"));
        //过滤字段map集合
        boolQueryBuilder = this.filterChoose(pageParam, boolQueryBuilder);
        queryBuilder.withQuery(boolQueryBuilder);
        //查询
        Page<Goods> goodsPage = goodRepository.search(queryBuilder.build());
        AggregatedPage<Goods> aggGoodPage = (AggregatedPage<Goods>) goodsPage;
        //获取出分组后的分类数据
        LongTerms categoryTerms = (LongTerms) aggGoodPage.getAggregation("category");
        //获取分类list中最多商品的分类id
        final List<Long> maxCount = new ArrayList<>(1);
        maxCount.add(0L);
        final List<Long> maxCountId = new ArrayList<>(1);
        maxCountId.add(0L);

        //循环遍历获取的分类Id通过Id 查询出分类存放入List
        List<Category> categoryList = categoryTerms.getBuckets().stream().map(bucket -> {
            if(maxCount.get(0)<bucket.getDocCount()){
                maxCount.set(0,bucket.getDocCount());
                maxCountId.set(0,bucket.getKeyAsNumber().longValue());
            }
            return  categoryClient.getCategoryById(bucket.getKeyAsNumber().longValue());
        }).collect(Collectors.toList());
        //获取出分组后的品牌数据
        LongTerms brandTerms = (LongTerms) aggGoodPage.getAggregation("brand");
        List<Brand> brandList = brandTerms.getBuckets().stream().map(bucket -> {
            return brandClient.getBrandByBid(bucket.getKeyAsNumber().longValue());
        }).collect(Collectors.toList());
        //高光设置
        queryBuilder.withHighlightFields(new HighlightBuilder.Field("all").preTags("<font color='red'>").postTags("</font>"));
        Map<Long, String> highMap = HighLightUtil.getHignLigntMap(template, queryBuilder.build(), Goods.class, "all");
        //循环设置高光 替换原数据
        goodsPage.forEach(goods -> {
            goods.setAll(highMap.get(goods.getId()));
        });
        //返回总页数
        Long total = goodsPage.getTotalElements();
        long totalPage = (long) (Math.ceil(total.doubleValue()/pageParam.getRows()));
        //返回规格
        List<Map<String, Object>> specList = this.getSpecList(maxCountId.get(0), pageParam);
        return  new SearchResult<Goods>(goodsPage.getTotalElements(),totalPage,goodsPage.getContent(),categoryList,brandList,specList);
    }
    
    private List<Map<String, Object>>  getSpecList(Long cid3 , PageParam pageParam){
    //通过分类cid查询出商品规格
        List<SpecParam> specParams = specClient.getParamsByCateGroupId(cid3, null, true,null);
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(pageParam != null && StringUtils.isNotEmpty(pageParam.getKey())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("all", pageParam.getKey()));
        }
        //过滤字段map集合
        boolQueryBuilder = this.filterChoose(pageParam, boolQueryBuilder);
        //存放进queryBuilder中
        queryBuilder.withQuery(boolQueryBuilder);
        //通过循环设置规格聚合分组
        specParams.forEach(specParam -> {
            queryBuilder.addAggregation(
                   AggregationBuilders.terms(specParam.getName()).field("specs."+specParam.getName()+".keyword")
            );
        });
        List<Map<String, Object>> specMapList = new ArrayList<>();
        //查询结果
        AggregatedPage<Goods> aggGoodsPage= (AggregatedPage<Goods>) goodRepository.search(queryBuilder.build());
        //循环遍历获取Bucket
        specParams.forEach(specParam -> {
            //创建map kv接收规格参数
            Map<String, Object> specMap = new HashMap<>();
           StringTerms specTerms = (StringTerms) aggGoodsPage.getAggregation(specParam.getName());
           //获取Bucket分组数据
            List<StringTerms.Bucket> specBuckets = specTerms.getBuckets();
            //将spec表的name当作key
            specMap.put("key",specParam.getName());
            //将分组的数据存放在list中
            List<String> values = specBuckets.stream().map(bucket -> {
                return bucket.getKeyAsString();
            }).collect(Collectors.toList());
            //将list放入map中
            specMap.put("values",values);
            specMapList.add(specMap);
        });
        return specMapList;
    }

    private  BoolQueryBuilder filterChoose(PageParam pageParam,BoolQueryBuilder boolQueryBuilder){
        Map<String, Object> filters = pageParam.getFilters();
        //创建条件过滤查询对象
        //遍历map
        if(filters != null && filters.size()>0){
            filters.forEach((key,value) ->{
                //拼接filter过滤条件
                if(key.equals("cid3") || key.equals("brandId")){
                    boolQueryBuilder.filter(QueryBuilders.matchQuery(key,value));
                }else{
                    boolQueryBuilder.filter(QueryBuilders.matchQuery("specs."+key+".keyword",value));
                }
            });
        }
        return  boolQueryBuilder;
    }

}
