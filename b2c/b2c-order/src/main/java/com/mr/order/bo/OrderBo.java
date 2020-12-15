package com.mr.order.bo;

import lombok.Data;

import java.util.List;

@Data
public class OrderBo {
    //商品list
    private List<CartBo> cartBoList;
    //地址id
    private Long addressId;
    //支付方式
    private Integer payMentType;
    //发票
    private Integer  invoiceType;
    //买家留言
    private String  buyerMessage;
}
