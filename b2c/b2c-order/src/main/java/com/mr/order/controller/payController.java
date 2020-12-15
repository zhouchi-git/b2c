package com.mr.order.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.mr.config.ApliPayConfig;
import com.mr.order.pojo.Order;
import com.mr.order.pojo.OrderDetail;
import com.mr.order.service.OrderService;
import com.mr.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

@Controller
public class payController {
    @Autowired
    private OrderService orderService;

    /**
     * 支付
     * @param orderId
     * @param response
     * @return
     * @throws AlipayApiException
     * @throws IOException
     */
    @GetMapping(value="pay/{orderId}")
    public String pay1(
            @PathVariable("orderId") Long orderId,
            HttpServletResponse response
    ) throws AlipayApiException, IOException {
        // 获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(ApliPayConfig.GATEWAYURL, ApliPayConfig.APP_ID,
                ApliPayConfig.MERCHANT_PRIVATE_KEY, "json", ApliPayConfig.CHARSET,
                ApliPayConfig.ALIPAY_PUBLIC_KEY,ApliPayConfig.SIGN_TYPE);


        // 设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //修改订单状态的页面
        alipayRequest.setNotifyUrl(ApliPayConfig.NOTIFY_URL);
        //成功支付后跳转的页面
        alipayRequest.setReturnUrl(ApliPayConfig.RETURN_URL);


        // 付款金额，必填
        Order order=orderService.queryById(orderId);

        OrderDetail orderDetail = orderService.getOrderDetailListByOrderId(orderId).get(0);
        String subject = orderDetail.getTitle();
        Collection<String> values = JsonUtils.parseMap(orderDetail.getOwnSpec(), String.class, String.class).values();
        String body = values.toString();
        String price = order.getActualPay().toString();
        String param;
        param= "{"  +
                "    \"out_trade_no\":\""+orderId.toString()+"\","  +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\","  +
                "    \"total_amount\":"+price.toString()+","  +
                "    \"subject\":\""+subject+"\","  +
                "    \"body\":\""+body+"\""  +
//                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\","  +
//                "    \"extend_params\":{"  +
//                "    \"sys_service_provider_id\":\"2088511833207846\""  +
//                "    }" +
                "  }" ; //填充业务参数
        System.out.println(param);
        alipayRequest.setBizContent(param);
        // 请求
        String form = alipayClient.pageExecute(alipayRequest).getBody();
        // System.out.println(result);
        response.setContentType("text/html; charset=gbk");
        PrintWriter out = response.getWriter();
        out.print(form);
        return null;
    }

    //支付完成后的操作
    @GetMapping("callback")
    public  void  callback(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String orderId = request.getParameter("out_trade_no");
        //修改订单状态
        orderService.updateStatus(Long.valueOf(orderId),2);
        //跳转页面
        response.sendRedirect("http://www.b2c.com/home-index.html");
    }
}
