package com.mr.test;

import com.mr.CartApplication;
import com.mr.bo.RedisCart ;
import com.mr.client.GoodsClient;
import com.mr.pojo.Sku;
import com.mr.utils.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CartApplication.class)
public class TestCart {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private  final  static  String SKU_KEY_ID="sku_key_id:";
    @Test
    public  void  addCart(){

        //查询出所有的sku
        List<Sku> skuList = goodsClient.queryListSku();
        skuList.forEach(sku -> {
            RedisCart cart = new RedisCart();
            BoundValueOperations<String, String> valueOps = redisTemplate.boundValueOps(SKU_KEY_ID + sku.getId());
            //填充cart数据
            cart.setSkuId(sku.getId());
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setStock(sku.getStock());
            cart.setEnable(sku.getEnable());
            cart.setImage(sku.getImages() == null?"":sku.getImages().split(",")[0]);
            //存放到redis
            valueOps.set(JsonUtils.serialize(cart));
        });


    }

}
