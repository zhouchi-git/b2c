package com.mr.order.mapper;

 import com.mr.order.pojo.OrderDetail;
 import org.apache.ibatis.annotations.Mapper;
 import org.apache.ibatis.annotations.Param;
 import org.apache.ibatis.annotations.Select;
 import tk.mybatis.mapper.common.special.InsertListMapper;

 import java.util.List;


@Mapper
public interface OrderDetailMapper extends tk.mybatis.mapper.common.Mapper<OrderDetail>, InsertListMapper<OrderDetail> {

}
