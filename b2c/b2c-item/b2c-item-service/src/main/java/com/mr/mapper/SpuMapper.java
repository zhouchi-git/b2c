package com.mr.mapper;

import com.mr.pojo.Spu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface SpuMapper extends  tk.mybatis.mapper.common.Mapper<Spu>{
    @Update("update tb_spu set tb_spu.saleable = 0 where id = #{id} ")
    void deleteGoods(Long id);

    @Update("update tb_spu set tb_spu.saleable = 1 where id = #{id} ")
    void showGoods(Long id);
}
