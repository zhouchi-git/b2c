package com.mr.mapper;

import com.mr.pojo.Stock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StockMapper extends tk.mybatis.mapper.common.Mapper<Stock> {
    @Update("UPDATE tb_stock set stock = stock -#{num} where sku_id= #{skuId}  and stock >= #{num}")
    int updateStockBySKuId(@Param("skuId") Long skuId, @Param("num") Integer num);
}
