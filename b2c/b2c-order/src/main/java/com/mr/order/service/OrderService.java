package com.mr.order.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mr.bo.UserInfo;
import com.mr.enums.ExceptionEnums;
import com.mr.exception.MyException;
import com.mr.order.bo.AddressBo;
import com.mr.order.bo.CartBo;
import com.mr.order.bo.OrderBo;
import com.mr.order.client.GoodsClient;
import com.mr.order.client.UserClient;
import com.mr.order.mapper.OrderDetailMapper;
import com.mr.order.mapper.OrderMapper;
import com.mr.order.mapper.OrderStatusMapper;
import com.mr.order.pojo.Order;
import com.mr.order.pojo.OrderDetail;
import com.mr.order.pojo.OrderStatus;
import com.mr.pojo.Sku;
import com.mr.utils.IdWorker;
import com.mr.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private UserClient userClient;
    @Autowired
    private  GoodsClient goodsClient;

    /**
     * 添加订单
     * @param userInfo
     * @param orderBo
     * @return
     */
    @Transactional
    public Long createOrder(UserInfo userInfo, OrderBo orderBo) {
        //定义总金额
        Long sumPrice = 0L;
        //填充order信息
        Order order = new Order();
        long orderId = new IdWorker().nextId();
        order.setOrderId(orderId);// id
          order.setPaymentType(orderBo.getPayMentType());// 支付类型，1、在线支付，2、货到付款
          order.setPostFee(0L);// 邮费
            Date date = new Date();
        order.setCreateTime(date);// 创建时间
//        private String shippingName;// 物流名称
//        private String shippingCode;// 物流单号
          order.setUserId(userInfo.getId());// 用户id
            order.setBuyerMessage(orderBo.getBuyerMessage());// 买家留言
        order.setBuyerNick(userInfo.getUsername());// 买家昵称
//        private Boolean buyerRate;// 买家是否已经评价
        //根据id查询出收货人信息
        AddressBo address = userClient.getAddressById(orderBo.getAddressId());
        order.setReceiver(address.getName());// 收货人全名
        order.setReceiverMobile(address.getPhone());// 移动电话
        order.setReceiverState(address.getState());// 省份
        order.setReceiverCity(address.getCity());// 城市
        order.setReceiverDistrict(address.getDistrict());// 区/县
        order.setReceiverAddress(address.getAddress());// 收货地址，如：xx路xx号
        order.setReceiverZip(address.getZipCode());// 邮政编码,如：310001
        order.setInvoiceType(orderBo.getInvoiceType());// 发票类型，0无发票，1普通发票，2电子发票，3增值税发票
        order.setSourceType(2);// 订单来源 1:app端，2：pc端，3：M端，4：微信端，5：手机qq端
        List<CartBo> cartBoList = orderBo.getCartBoList();
        for (CartBo cartBo: cartBoList){
            //根据skuId查询出商品信息
            Sku sku = goodsClient.getSkuById(cartBo.getSkuId());
            cartBo.setSku(sku);
            //判断所购买的商品数量是否大于库存 防止恶意修改参数
            if(sku.getStock()<cartBo.getNum()){
                new MyException(ExceptionEnums.SKU_STOCK_IS_NO);
            }
            if(!sku.getEnable()){
                new MyException(ExceptionEnums.SKU_ENABLE_IS_NO);
            }
            //计算总金额
            sumPrice+=sku.getPrice()*cartBo.getNum();
        }
        //存放总金额
        order.setTotalPay(sumPrice);// 总金额
//        private Long actualPay;// 实付金额
        order.setActualPay(sumPrice-order.getPostFee());
        //添加订单到mysql
        orderMapper.insertSelective(order);

        cartBoList.forEach(cartBo -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);// 订单id
            orderDetail.setSkuId(cartBo.getSkuId());// 商品id
            orderDetail.setNum(cartBo.getNum());// 商品购买数量
            orderDetail.setTitle(cartBo.getSku().getTitle());// 商品标题
            orderDetail.setPrice(cartBo.getSku().getPrice());// 商品单价
            orderDetail.setOwnSpec(cartBo.getSku().getOwnSpec());// 商品规格数据
            orderDetail.setImage(cartBo.getSku().getImages() == null?"":cartBo.getSku().getImages().split(",")[0]);// 图片
            //添加订单规格到mysql
            orderDetailMapper.insertSelective(orderDetail);
        });
        //添加订单状态
        OrderStatus orderStatus = new OrderStatus();
            orderStatus.setOrderId(orderId);
            orderStatus.setStatus(1);//状态：1、未付款 2、已付款,未发货 3、已发货,未确认 4、交易成功 5、交易关闭 6、已评价',
            orderStatus.setCreateTime(date);// 创建时间
//        private Date paymentTime;// 付款时间

//        private Date consignTime;// 发货时间

//        private Date endTime;// 交易结束时间

//        private Date closeTime;// 交易关闭时间

//        private Date commentTime;// 评价时间
        orderStatusMapper.insertSelective(orderStatus);

        //修改库存
        cartBoList.forEach( cartBo -> {
            Integer updateNum = goodsClient.updateStockBySKuId(cartBo.getSkuId(), cartBo.getNum());
            try {
                if(updateNum == 0){
                    System.out.println(updateNum);
                    throw new RuntimeException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        });
        //bug数据库库存更改后需要同步到缓存中
        return orderId;
    }

    /**
     * 分页查询订单以及根据状态查询
     * @param page
     * @param rows
     * @param status
     * @param userInfo
     * @return
     */

    public PageResult<Order> orderPage(Integer page, Integer rows, Integer status, UserInfo userInfo) {
        //分页
        try {
            // 设置分页等其起始值，每页条数
            PageHelper.startPage(page, rows);

            // 创建查询条件用户id和状态，由于需要三表联查，sql注解拼接麻烦，所以采用了xml形式
            Page<Order> pageInfo = (Page<Order>) this.orderMapper.queryOrderList(userInfo.getId(), status);

            return new PageResult<Order>(pageInfo.getTotal(), pageInfo);
        } catch (Exception e) {

            return null;
        }
    }

    /**
     * 根据orderId 获取 OrderDetailList
     * @param id
     * @return
     */
    public List<OrderDetail> getOrderDetailListByOrderId(Long id) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(id);
        return  orderDetailMapper.select(orderDetail);
    }

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    public Order queryById(Long id) {
        // 查询订单
        Order order = orderMapper.selectByPrimaryKey(id);

        // 查询订单详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderId(id);
        List<OrderDetail> details = orderDetailMapper.select(detail);
        order.setOrderDetails(details);

        // 查询订单状态
        OrderStatus status = orderStatusMapper.selectByPrimaryKey(order.getOrderId());
        order.setStatus(status.getStatus());
        return order;
    }

    /**
     * 修改订单状态
     * @param id
     * @param status
     * @return
     */
    @Transactional
    public Boolean updateStatus(Long id, Integer status) {
        OrderStatus record = new OrderStatus();
        record.setOrderId(id);
        record.setStatus(status);
        // 根据状态判断要修改的时间
        switch (status) {
            case 2:
                record.setPaymentTime(new Date());// 付款
                break;
            case 3:
                record.setConsignTime(new Date());// 发货
                break;
            case 4:
                record.setEndTime(new Date());// 确认收获，订单结束
                break;
            case 5:
                record.setCloseTime(new Date());// 交易失败，订单关闭
                break;
            case 6:
                record.setCommentTime(new Date());// 评价时间
                break;
            default:
                return null;
        }
        int count = orderStatusMapper.updateByPrimaryKeySelective(record);
        return count == 1;
    }
}
