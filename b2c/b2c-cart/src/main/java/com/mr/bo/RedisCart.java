package com.mr.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisCart {
    //private Long userId;// 用户id
    private Long skuId;// 商品id
    private String title;// 标题
    private String image;// 图片
    private Long price;// 价格
    private Integer num;// 购买数量
    private String ownSpec;// 商品规格参数
    private Boolean enable;//上下架
    private Integer stock;//库存
}
