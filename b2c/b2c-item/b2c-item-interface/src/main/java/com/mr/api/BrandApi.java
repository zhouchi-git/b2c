package com.mr.api;

import com.mr.pojo.Brand;
import com.mr.utils.PageResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("brand")
public interface BrandApi {

    /**
     * 查询所有
     * @param
     * @return
     */
    @GetMapping("list")
    PageResult brandList(
            @RequestParam(value = "page",required = false) Integer page,
            @RequestParam(value = "rows",required = false) Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",required = false) Boolean desc,
            @RequestParam(value = "key",required = false) String key
    );

    /**
     * 添加
     * @param brand
     * @param categories
     * @return
     */
    @PostMapping("addOrBrand")
    Void addBrand(@RequestParam Brand brand,@RequestParam String categories);

    /**
     * 修改
     * @param brand
     * @param categories
     * @return
     */
    @PutMapping("addOrBrand")
    Void updateBrand(@RequestParam Brand brand,@RequestParam String categories);

    /**
     * 删除
     * @param bid
     * @return
     */
    @DeleteMapping("delBrand/{bid}")
    Void delBrand(@PathVariable("bid") Long bid);

    /**
     * 根据cid 查询品牌
     * @param cid
     * @return
     */
    @GetMapping("getBrandById")
    List<Brand> getBrandById(@RequestParam  Long cid);

    /**
     * 根据bid 查询品牌
     * @param bid
     * @return
     */
    @GetMapping("getBrandByBid")
    Brand getBrandByBid(@RequestParam Long bid);

}
