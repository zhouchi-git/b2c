package com.mr.mapper;

import com.mr.pojo.Category_Brand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface CategoryBrandMapper extends tk.mybatis.mapper.common.Mapper<Category_Brand> {
    @Select("SELECT tb_category_brand.brand_id from tb_category_brand where category_id = #{cid} ")
    List<Long> selectByCategory_BrandByCategoryId(@Param("cid") Long cid);
}
