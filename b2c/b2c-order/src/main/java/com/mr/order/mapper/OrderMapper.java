package com.mr.order.mapper;

 import com.mr.order.pojo.Order;
 import org.apache.ibatis.annotations.Mapper;
 import org.apache.ibatis.annotations.Param;

 import java.util.List;


@Mapper
public interface OrderMapper extends tk.mybatis.mapper.common.Mapper<Order> {
 /**
  * 分页查询
  * @param id
  * @param status
  * @return
  */
 List<Order> queryOrderList(@Param("id") Long id,@Param("status") Integer status);
}
