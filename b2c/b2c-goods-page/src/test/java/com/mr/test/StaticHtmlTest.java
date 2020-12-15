package com.mr.test;

import com.mr.GoodPageApplication;
import com.mr.client.GoodsClient;
import com.mr.pojo.Spu;
import com.mr.service.FileStaticService;
import com.mr.utils.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GoodPageApplication.class)
public class StaticHtmlTest {
    @Autowired
    private FileStaticService fileStaticService;
    @Autowired
    private GoodsClient goodsClient;
    @Test
    public  void  creteHtml(){
        PageResult<Spu> spuPageResult = goodsClient.spuList(0, 200, "id", false, null, true);
        List<Spu> spus = spuPageResult.getItems();
        spus.forEach(spu ->{
            fileStaticService.staticGoodsHtml(spu.getId());
        });

    }
}
