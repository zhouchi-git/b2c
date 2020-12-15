package com.mr.controller;

import com.mr.pojo.Brand;
import com.mr.service.BrandService;
import com.mr.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 查询所有
     * @param
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<PageResult> brandList(
            @RequestParam(value = "page",required = false) Integer page,
            @RequestParam(value = "rows",required = false) Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",required = false) Boolean desc,
            @RequestParam(value = "key",required = false) String key
    ){
        PageResult pageResult = brandService.brandList(page, rows, sortBy, desc, key);
        if(pageResult == null || pageResult.getItems().size() == 0){
           return   new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return  ResponseEntity.ok(pageResult);

    }

    /**
     * 添加
     * @param brand
     * @param categories
     * @return
     */
    @PostMapping("addOrBrand")
    public ResponseEntity<Void> addBrand(Brand brand,String categories){
        brandService.addOrBrand(brand,categories);
        return  ResponseEntity.ok(null);
    }

    /**
     * 修改
     * @param brand
     * @param categories
     * @return
     */
    @PutMapping("addOrBrand")
    public ResponseEntity<Void> updateBrand(Brand brand,String categories){
        brandService.addOrBrand(brand,categories);
        return  ResponseEntity.ok(null);
    }

    /**
     * 删除
     * @param bid
     * @return
     */
    @DeleteMapping("delBrand/{bid}")
    public ResponseEntity<Void> delBrand(@PathVariable("bid") Long bid){

        brandService.delBrand(bid);
        return  ResponseEntity.ok(null);
    }

    /**
     * 根据cid 查询品牌
     * @param cid
     * @return
     */
    @GetMapping("getBrandById")
    public ResponseEntity<List<Brand>> getBrandById(@RequestParam Long cid){

        return  ResponseEntity.ok( brandService.getBrandById(cid));
    }

    /**
     * 根据bid 查询品牌
     * @param bid
     * @return
     */
    @GetMapping("getBrandByBid")
    public ResponseEntity<Brand> getBrandByBid(@RequestParam Long bid){

        return  ResponseEntity.ok( brandService.getBrandByBid(bid));
    }


}
