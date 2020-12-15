package com.mr.controller;

import com.mr.bo.Cart;
import com.mr.bo.UserInfo;
import com.mr.config.JwtConfig;
import com.mr.service.CartService;
import com.mr.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 添加购物车
     * @param token
     * @param cart
     * @return
     */
    @PostMapping("addCart")
    public ResponseEntity<Void> addCart(@CookieValue("B2C_TOKEN") String  token, @RequestBody Cart cart){
        try {
            UserInfo info = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            cartService.addCart(cart,info);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  ResponseEntity.ok(null);
    }

    /**
     * 查询购物车
     * @param token
     * @return
     */
    @GetMapping("getCartList")
    public ResponseEntity<List<Cart>> getCartList(@CookieValue("B2C_TOKEN") String  token){
        List<Cart> cartList =null;
        try {
            UserInfo info = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
             cartList = cartService.getCartList(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  ResponseEntity.ok(cartList);
    }

    /**
     * 修改数量
     * @param token
     * @return
     */
    @PutMapping("updateNum")
    public  ResponseEntity<Void> updateNum(
                        @RequestParam("skuId") Long skuId,
                        @RequestParam("num") Integer num,
                        @CookieValue("B2C_TOKEN") String  token){
        try {
            UserInfo info = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            cartService.updateNum(info,skuId,num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(null);
    }

    /**
     * 删除购物车数据
     * @param token
     * @return
     */
    @DeleteMapping("deleteCart")
    public  ResponseEntity<Void> deleteCart(
            @RequestParam("skuId") Long skuId,
            @CookieValue("B2C_TOKEN") String  token){
        try {
            UserInfo info = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            cartService.deleteCart(info,skuId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(null);
    }
}
