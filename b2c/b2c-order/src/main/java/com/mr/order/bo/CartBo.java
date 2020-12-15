package com.mr.order.bo;

import com.mr.pojo.Sku;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartBo {
    private Long skuId;// 商品id
    private Integer num;// 购买数量\
    private Sku sku;

}