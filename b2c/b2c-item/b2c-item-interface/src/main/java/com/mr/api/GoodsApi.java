package com.mr.api;

import com.mr.pojo.*;
import com.mr.utils.PageResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("goods")
public interface GoodsApi {


    /**
     * 商品展示
     * @param
     * @return
     */
    @GetMapping("list")
    PageResult<Spu> spuList(
            @RequestParam(value = "page",required = false) Integer page,
            @RequestParam(value = "rows",required = false) Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",required = false) Boolean desc,
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "saleable",required = false) Boolean saleable
    );

    /**
     * 商品的添加
     * @param spubo
     * @return
     */
    @PostMapping
    Void addGoods(@RequestBody SpuBo spubo);

    /**
     * 修改商品
     * @param spubo
     * @return
     */
    @PutMapping
    Void updateGoods(@RequestBody SpuBo spubo);

    /**
     * 商品的下架
     * @param id
     * @return
     */
    @PutMapping("delGoods")
    Void deleteGoods(@RequestParam("id") Long id);

    /**
     * 商品的上架
     * @param id
     * @return
     */
    @PutMapping("showGoods")
    Void showGoods(@RequestParam("id") Long id);

    /**
     * 查询商品详情
     * @param spu_id
     * @return
     */
    @GetMapping("detail/{spu_id}")
   SpuDetail getDetail(@PathVariable("spu_id") Long spu_id);

    /**
     * 根据spu_id查询skus
     * @param spu_id
     * @return
     */

    @GetMapping("getSkus/{spu_id}")
    List<Sku> getSkus(@PathVariable("spu_id") Long spu_id);

    /**
     * 根据id查询sku
     * @param id
     * @return
     */
    @GetMapping("getSku/{id}")
    Sku getSkuById(@PathVariable("id") Long id);

    /**
     * 根据spu_id查询skus(单表)
     * @param spu_id
     * @return
     */
    @GetMapping("getSkuBySpuIdDan")
    List<Sku> getSkuBySpuId_Dan(@RequestParam("spu_id") Long spu_id);

    /**
     * 根据id查询spu
     * @param id
     * @return
     */
    @GetMapping("getSpuById/{id}")
    Spu getSpuById(@PathVariable("id") Long id);


    /**
     * 根据spu_id  查询商品详情
     * @param spuId
     * @return
     */
    @GetMapping("getSpuDetailBySpuId")
    SpuDetail getSpuDetailBySpuId(@RequestParam Long spuId);

    /**
     * 根据商品id 查询库存
     * @param skuId
     * @return
     */
    @GetMapping("getStockBySkuId")
    Stock getStockBySkuId(@RequestParam Long skuId);

    /**
     * 查询所有的sku
     * @return
     */
    @GetMapping("queryListSku")
    List<Sku> queryListSku();

    /**
     *根据skuId 修改stock库存
     * @param skuId
     * @param num
     * @return
     */
    @PutMapping("updateStockBySKuId")
    Integer updateStockBySKuId(@RequestParam Long skuId , @RequestParam Integer num);

}
