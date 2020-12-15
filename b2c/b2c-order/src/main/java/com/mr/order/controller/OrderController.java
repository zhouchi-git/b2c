package com.mr.order.controller;

import com.mr.bo.UserInfo;
import com.mr.config.JwtConfig;
import com.mr.order.bo.OrderBo;
import com.mr.order.pojo.Order;
import com.mr.order.service.OrderService;
import com.mr.util.JwtUtils;
import com.mr.utils.PageResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private JwtConfig jwtConfig;

    /**
     *
     *创建订单
     * @param orderBo 订单对象（地址id，skuid与数量，支付方式）
     * @return 订单编号
     */
    @ApiOperation(value = "创建订单接口，返回订单编号",notes = "创建订单")
    @ApiImplicitParam(name = "OrderBo", required = true, value = "订单的json对象,包含skuid，购买数量")
    @PostMapping("createOrder")
    public ResponseEntity<String> createOrder(
            @CookieValue("B2C_TOKEN") String token,
            @RequestBody OrderBo orderBo
            ){
        System.out.println(orderBo);
        Long orderId = null;
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            orderId = orderService.createOrder(userInfo, orderBo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  ResponseEntity.ok(orderId.toString());
    }

    /**
     * 分页查询订单以及根据状态查询
     * @param page
     * @param rows
     * @param status
     * @return
     */
    @GetMapping("orderPage")
    @ApiOperation(value = "分页查询当前用户订单，并且可以根据订单状态过滤", notes = "分页查询当前用户订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页", defaultValue = "1", type = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页大小", defaultValue = "5", type = "Integer"),
            @ApiImplicitParam(name = "status", value = "订单状态：1未付款，2已付款未发货，3已发货未确认，4已确认未评价，5交易关闭，6交易成功，已评价", type = "Integer"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "订单的分页结果"),
            @ApiResponse(code = 404, message = "没有查询到结果"),
            @ApiResponse(code = 500, message = "查询失败"),
    })
    public ResponseEntity<PageResult<Order>> orderPage(
            @RequestParam(value = "page", required = true, defaultValue = "1") Integer page, //当前页数
            @RequestParam(value = "rows", required = true, defaultValue = "5") Integer rows,  //每页几条
            @RequestParam(value = "status" ,required = false) Integer status, //根据订单状态查询
            @CookieValue("B2C_TOKEN") String token
            ){
        PageResult<Order> orderList = null;
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
             orderList = orderService.orderPage(page, rows, status, userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  ResponseEntity.ok(orderList);
    }

    /**
     * 根据订单编号查询订单
     * @param id
     * @return
     */
    @GetMapping("getOrder/{id}")
    @ApiOperation(value = "根据订单编号查询，返回订单对象", notes = "查询订单")
    @ApiImplicitParam(name = "id", required = true, value = "订单的编号")
    public ResponseEntity<Order> queryOrderById(@PathVariable("id") Long id) {
        Order order = this.orderService.queryById(id);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(order);
    }

    /**
     * 更新订单状态
     *
     * @param id
     * @param status
     * @return
     */
    @PutMapping("updateOrder/{id}/{status}")
    @ApiOperation(value = "更新订单状态", notes = "更新订单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单编号", type = "Long"),
            @ApiImplicitParam(name = "status", value = "订单状态：1未付款，2已付款未发货，3已发货未确认，4已确认未评价，5交易关闭，6交易成功，已评价", type = "Integer"),
    })

    @ApiResponses({
            @ApiResponse(code = 204, message = "true：修改状态成功；false：修改状态失败"),
            @ApiResponse(code = 400, message = "请求参数有误"),
            @ApiResponse(code = 500, message = "查询失败")
    })
    public ResponseEntity<Boolean> updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        Boolean boo = orderService.updateStatus(id, status);
        if (boo == null) {
            // 返回400
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // 返回204
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
