package com.mr.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mr.client.*;
import com.mr.pojo.*;
import com.mr.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsPageService {
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

    public  Map<String, Object>  goodsxxxx(Long spuId){
        Map<String, Object> map = new HashMap<>();
        //获取spu
        Spu spu = goodsClient.getSpuById(spuId);
        //获取品牌信息
        Brand brand = brandClient.getBrandByBid(spu.getBrandId());
        //获取分类
        List<Category> categoryList = categoryClient.getCategoryListByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //获取sku与stock
        List<Sku> skuList = goodsClient.getSkuBySpuId_Dan(spuId);

        skuList = skuList.stream().map(sku -> {
            Stock stock = goodsClient.getStockBySkuId(sku.getId());
            sku.setStock(stock.getStock());
            return  sku;
        }).collect(Collectors.toList());

        //获取规格组(规格组中包含通用属性)
        List<SpecGroup> groupList = groupClient.getGroupByCateGroupId(spu.getCid3());
        //获取通用属性
        groupList = groupList.stream().map(group ->{
            //根据规格Id查询不同规格下的通用属性
            List<SpecParam> generalList = specClient.getParamsByCateGroupId(spu.getCid3(), group.getId(), null, null);
            group.setSpecParamList(generalList);
            return group;
        }).collect(Collectors.toList());
        // 获取特有属性
        List<SpecParam> uniqueList = specClient.getParamsByCateGroupId(spu.getCid3(), null, null, false);
        HashMap<Long, Object> uniqueListMap = new HashMap<>();
        uniqueList.forEach(unique->{
            uniqueListMap.put(unique.getId(),unique.getName());
        });

        //获取详情
        SpuDetail spuDetail = goodsClient.getSpuDetailBySpuId(spu.getId());

        map.put("spu",spu);
        map.put("brand",brand);
        map.put("categoryList",categoryList);
        map.put("skuList",skuList);
        map.put("groupList",groupList);
        map.put("uniqueListMap",uniqueListMap);
        map.put("spuDetail",spuDetail);
        return map;
    }

}
