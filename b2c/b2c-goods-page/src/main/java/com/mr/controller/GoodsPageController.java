package com.mr.controller;

import com.mr.service.GoodsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("item")
public class GoodsPageController {
    @Autowired
    private GoodsPageService goodsPageService;
    @GetMapping("{id}.html")
    public String getGoodsPage(@PathVariable("id") Long id, ModelMap model){
        model.putAll(goodsPageService.goodsxxxx(id));
        return "item";
    }
}
