package com.mr.controller;

import com.mr.bo.PageParam;
import com.mr.pojo.Goods;
import com.mr.service.GoodsService;
import com.mr.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> goodsPage(@RequestBody  PageParam pageParam){

        return  ResponseEntity.ok(goodsService.queryGoodsPage(pageParam));
    }
}
