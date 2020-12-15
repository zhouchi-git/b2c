package com.mr.test;

import com.mr.SearchApplication;
import com.mr.client.GoodsClient;
import com.mr.dao.GoodRepository;
import com.mr.pojo.Goods;
import com.mr.pojo.Spu;
import com.mr.service.GoodsService;
import com.mr.utils.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class)
public class SearchTest {
    @Autowired
    private ElasticsearchTemplate template;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodRepository goodRepository;
    @Autowired
    private GoodsClient goodsClient;

    @Test
    public  void  createIndexName(){
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }

    @Test
    public void getGoodsById(){
        Goods goods = goodsService.getGoods(2L);
        goodRepository.save(goods);
    }

    @Test
    public  void  loadGoods(){
        List<Goods> goodsArrayList = new ArrayList<>();
        PageResult<Spu> spuPageResult = goodsClient.spuList(0,200,null,true,null,true);
        spuPageResult.getItems().forEach(spu -> {
            goodsArrayList.add(goodsService.getGoods(spu.getId()));
        });
        goodRepository.saveAll(goodsArrayList);
    }
}
