package com.mr.mapper;

import com.mr.pojo.Sku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@Mapper
public interface SkuMapper extends tk.mybatis.mapper.common.Mapper<Sku>{
    @Select("SELECT tb_sku.*,tb_stock.* from tb_sku LEFT JOIN tb_stock on tb_sku.id = tb_stock.sku_id where tb_sku.spu_id=#{spuId}")
    List<Sku> selectSkusBySpuId(@Param("spuId") Long spuId);
}
