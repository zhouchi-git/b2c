package com.mr.mapper;

import com.mr.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BrandMapper extends tk.mybatis.mapper.common.Mapper<Brand>{
    @Delete("DELETE from tb_brand WHERE tb_brand.id=#{bid}")
    void deleteByBrandId(@Param("bid") Long bid);
    @Delete("DELETE from tb_category_brand where tb_category_brand.brand_id = #{bid}")
    void delCategory_BrandByBrandId(@Param("bid") Long bid);

    @Select("SELECT * FROM tb_brand  where tb_brand.id in (${ids})")
    List<Brand> selectBrandByIds(@Param("ids") String ids);
}
