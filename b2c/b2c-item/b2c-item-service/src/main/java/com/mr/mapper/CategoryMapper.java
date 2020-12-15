package com.mr.mapper;


import com.mr.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper extends tk.mybatis.mapper.common.Mapper<Category> {

    @Select(" SELECT  MAX(id) from tb_category")
    Long getMaxId();
    @Select("SELECT * from tb_category  where tb_category.id in (SELECT cb.category_id from tb_brand  b INNER JOIN tb_category_brand cb ON b.id=cb.brand_id  where b.id = #{bid})")
    List<Category> getCategoryByBrandId(@Param("bid") Long bid);
    @Select("select name from tb_category where id in(${ids})")
    List<String> selectCatGoryNameByIds(@Param("ids") String ids);
    @Select("select * from tb_category where id in(${ids})")
    List<Category> getCategoryListByIds(@Param("ids")String ids);
}
