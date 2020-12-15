package com.mr.service;

import org.bouncycastle.jcajce.provider.symmetric.TLSKDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

@Component
public class FileStaticService {
    @Autowired
    private  GoodsPageService goodsPageService;//用来获取数据
    @Autowired
    private TemplateEngine templateEngine;//模板引擎
    @Value("${b2c.thymeleaf.destPath}")
    private  String destPath;//文件存放路径

    public  void  staticGoodsHtml(Long id){
        //    Context：运行上下文
        //通过id 获取到返回页面的数据
        Map<String, Object> map = goodsPageService.goodsxxxx(id);
        Context context = new Context();
        //将数据放入上下文对象中
        context.setVariables(map);
        //生成模板存放的文件位置并输入
        File file = new File(destPath,id+".html");
        PrintWriter printWriter =null;
        try {
            printWriter = new PrintWriter(file,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        templateEngine.process("item",context,printWriter);
    }
}
