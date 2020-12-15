package com.mr.service;

import com.mr.bo.Cart;
import com.mr.bo.RedisCart;
import com.mr.bo.UserInfo;
import com.mr.client.GoodsClient;
import com.mr.pojo.Sku;
import com.mr.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private StringRedisTemplate redisTemplate;

    //用户存放到购物车的key
    private final static String KEY_PRE = "b2c-cart-redis:";
    //商品存放购物车的key
    private  final  static  String SKU_KEY_ID="sku_key_id:";

    /**
     *添加到购物车
     * @param cart
     * @param info
     */
    public void addCart(Cart cart, UserInfo info) {
        //判断是否存在数据
        String key=KEY_PRE+info.getId();
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        //获取到传过来的num
        Integer num = cart.getNum();
        //如果这个商品的id存在就只改变数量
        if(hashOps.hasKey(cart.getSkuId().toString())){
            String cartJson = hashOps.get(cart.getSkuId().toString()).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);
            cart.setNum(cart.getNum()+num);
        }else{
            //填充cart数据
            Sku sku = goodsClient.getSkuById(cart.getSkuId());
            cart.setUserId(info.getId());
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setStock(sku.getStock());
            cart.setEnable(sku.getEnable());
            cart.setImage(sku.getImages() == null?"":sku.getImages().split(",")[0]);
        }
        //存放到redis
        hashOps.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
    }

    /**
     * 查询购物车
     * @param info
     * @return
     */
    public List<Cart> getCartList(UserInfo info) {
        //通过key查询出所有的values
        String key = KEY_PRE + info.getId();
        //绑定key
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        //获取key下的所有的values
        return hashOps.values().stream().map(o -> {
            //将json转换为对象
            return this.CheckCart(o);
        }).collect(Collectors.toList());
    }

    /**
     * 校验购物车数据是否更新
     * @return
     */
    private   Cart  CheckCart(Object o){
        //json解析为Cart
        Cart cart = JsonUtils.parse(o.toString(), Cart.class);
        //从redis中获取商品
        BoundValueOperations<String, String> valueOps = redisTemplate.boundValueOps(SKU_KEY_ID + cart.getSkuId());
        //redis中的商品信息
        String cartJson = valueOps.get();
        //判断缓存中是否存在
        if(cartJson == null){
            RedisCart redisCart= new RedisCart();
            Sku sku = goodsClient.getSkuById(cart.getSkuId());
            //填充cart数据
            redisCart.setSkuId(sku.getId());
            redisCart.setPrice(sku.getPrice());
            redisCart.setTitle(sku.getTitle());
            redisCart.setOwnSpec(sku.getOwnSpec());
            redisCart.setStock(sku.getStock());
            redisCart.setEnable(sku.getEnable());
            redisCart.setImage(sku.getImages() == null?"":sku.getImages().split(",")[0]);
            //存放到redis
            valueOps.set(JsonUtils.serialize(redisCart));
        }
        cartJson = valueOps.get();
        RedisCart redisCart = JsonUtils.parse(cartJson, RedisCart.class);
        //判断价格是否更新
        if(cart.getPrice().intValue()!=redisCart.getPrice().intValue()){
           Long oldPrice = cart.getPrice();
           cart.setPrice(redisCart.getPrice());
           cart.setOldPrice(oldPrice);
        }
        //判断是否上下架
        cart.setEnable(redisCart.getEnable());
        //覆盖库存
        cart.setStock(redisCart.getStock());
        return  cart;
    }
    /**
     * 修改数量
     * @param info
     */
    public void updateNum(UserInfo info, Long skuId,Integer num) {
        String key = KEY_PRE+info.getId();
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        String cartJson = hashOps.get(skuId.toString()).toString();
        Cart cart = JsonUtils.parse(cartJson, Cart.class);
        cart.setNum(num);
        hashOps.put(skuId.toString(),JsonUtils.serialize(cart));
    }

    /**
     * 删除购物车数据
     * @param info
     */
    public void deleteCart(UserInfo info, Long skuId) {
        String key = KEY_PRE+info.getId();
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        hashOps.delete(skuId.toString());
    }
}
