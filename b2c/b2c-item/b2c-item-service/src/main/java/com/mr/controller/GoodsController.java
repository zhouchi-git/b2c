package com.mr.controller;

import com.mr.pojo.*;
import com.mr.service.GoodsService;
import com.mr.utils.PageResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    /**
     * 商品展示
     * @param
     * @return
     */

    @GetMapping("list")
    public ResponseEntity<PageResult<Spu>> spuList(
            @RequestParam(value = "page",required = false) Integer page,
            @RequestParam(value = "rows",required = false) Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",required = false) Boolean desc,
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "saleable",required = false) Boolean saleable
    ){
        PageResult<Spu> spuPageResult = goodsService.spuList(page, rows, sortBy, desc,key, saleable);
        return  ResponseEntity.ok(spuPageResult);
    }

    /**
     * 商品的添加
     * @param spubo
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addGoods(@RequestBody SpuBo spubo){
        goodsService.addOrUpdateGoods(spubo);

        return  ResponseEntity.ok(null);
    }

    /**
     * 修改商品
     * @param spubo
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spubo){
        goodsService.addOrUpdateGoods(spubo);
        return  ResponseEntity.ok(null);
    }

    /**
     * 商品的下架
     * @param id
     * @return
     */
    @PutMapping("delGoods")
    public ResponseEntity<Void> deleteGoods(@RequestParam("id") Long id){
        goodsService.deleteGoods(id);
        return  ResponseEntity.ok(null);
    }

    /**
     * 商品的上架
     * @param id
     * @return
     */
    @PutMapping("showGoods")
    public ResponseEntity<Void> showGoods(@RequestParam("id") Long id){
        goodsService.showGoods(id);
        return  ResponseEntity.ok(null);
    }

    /**
     * 查询商品详情
     * @param spu_id
     * @return
     */
    @GetMapping("detail/{spu_id}")
    public ResponseEntity<SpuDetail> getDetail(@PathVariable("spu_id") Long spu_id){
        SpuDetail detail = goodsService.getDetail(spu_id);
        return  ResponseEntity.ok(detail);
    }

    /**
     * 根据spu_id查询skus
     * @param spu_id
     * @return
     */
    @GetMapping("getSkus/{spu_id}")
    public ResponseEntity<List<Sku>> getSkus(@PathVariable("spu_id") Long spu_id){
        List<Sku> skus = goodsService.getSkus(spu_id);
        return  ResponseEntity.ok(skus);
    }

    /**
     * 根据id查询sku
     * @param id
     * @return
     */
    @GetMapping("getSku/{id}")
    public ResponseEntity<Sku> getSkuById(@PathVariable("id") Long id){
        Sku sku = goodsService.getSkuById(id);
        return  ResponseEntity.ok(sku);
    }

    /**
     * 查询所有的sku
     * @return
     */
    @GetMapping("queryListSku")
    public ResponseEntity<List<Sku>> queryListSku(){
        List<Sku>  skuList = goodsService.queryListSku();
        return  ResponseEntity.ok(skuList);
    }

    /**
     * 根据spu_id查询skus(单表)
     * @param spu_id
     * @return
     */
    @GetMapping("getSkuBySpuIdDan")
    public ResponseEntity<List<Sku>> getSkuBySpuId_Dan(@RequestParam("spu_id") Long spu_id){
        List<Sku> skus = goodsService.getSkuBySpuId_Dan(spu_id);
        return  ResponseEntity.ok(skus);
    }

    /**
     * 根据id查询spu
     * @param id
     * @return
     */
    @GetMapping("getSpuById/{id}")
    public ResponseEntity<Spu> getSpuById(@PathVariable("id") Long id){
        Spu spu = goodsService.getSpuById(id);
        return  ResponseEntity.ok(spu);
    }

    /**
     * 根据spu_id  查询商品详情
     * @param spuId
     * @return
     */
    @GetMapping("getSpuDetailBySpuId")
    public  ResponseEntity<SpuDetail> getSpuDetailBySpuId(@RequestParam Long spuId){

        return  ResponseEntity.ok(goodsService.getSpuDetailBySpuId(spuId));
    }

    /**
     * 根据商品id 查询库存
     * @param skuId
     * @return
     */
    @GetMapping("getStockBySkuId")
    public  ResponseEntity<Stock> getStockBySkuId(@RequestParam Long skuId){

        return  ResponseEntity.ok(goodsService.getStockBySkuId(skuId));
    }

    /**
     *根据skuId 修改stock库存
     * @param skuId
     * @param num
     * @return
     */
    @PutMapping("updateStockBySKuId")
    public ResponseEntity<Integer> updateStockBySKuId(@RequestParam Long skuId , @RequestParam Integer num){

        return  ResponseEntity.ok(goodsService.updateStockBySKuId(skuId,num));
    }
}
