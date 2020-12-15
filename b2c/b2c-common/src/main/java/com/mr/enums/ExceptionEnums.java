package com.mr.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum  ExceptionEnums {
    //定义枚举
    PRICE_CANNOT_IS_NULL(400,"价格不能为空！"),
    NAME_CANNOT_IS_NULL(500,"姓名不能为空!"),
    CATEGORY_CANNOT_IS_NULL(200,"删除失败！"),
    CATEGORY_DELETE_BY_PID(500,"还有子类不能删除！"),
    CATEGORY_ADD(200,"添加失败！"),
    CATEGORY_UPDATE(200,"修改失败！"),
    SKU_STOCK_IS_NO(500,"恶意修改数据！封号处理！"),
    SKU_ENABLE_IS_NO(500,"恶意修改数据！封号处理！"),
    STOCK_IS_NO(500,""),
    ;


    private  Integer status;
    private  String message;

}
