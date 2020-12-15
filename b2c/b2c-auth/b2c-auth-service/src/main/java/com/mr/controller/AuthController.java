package com.mr.controller;

import com.mr.bo.UserInfo;
import com.mr.config.JwtConfig;
import com.mr.pojo.User;
import com.mr.service.AuthService;
import com.mr.util.JwtUtils;
import com.mr.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.ipc.netty.http.server.HttpServerResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 登录验证
     * @param user
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<Void> login(User user, HttpServletRequest request,HttpServletResponse  response){

        try {
            String token = authService.login(user);
            if(token != null){
               //将token存入cookie中
                CookieUtils.setCookie(request, response,jwtConfig.getCookieName(),token,jwtConfig.getCookieMaxAge(),true);
                System.out.println("ok");
            }else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {

            System.out.println("yichang");
            e.printStackTrace();
        }
        return  ResponseEntity.ok(null);
    }

    ///auth/verify
    @GetMapping("verify")
    //通过token 解码获取当前登录的用户
    public  ResponseEntity<UserInfo> verify(
                        @CookieValue("B2C_TOKEN") String token,
                        HttpServletRequest request,
                        HttpServletResponse response
    ){

        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            //解码成功后要刷新cookie过期时间
            String newToken = JwtUtils.generateToken(userInfo, jwtConfig.getPrivateKey(), jwtConfig.getCookieMaxAge());
            //重新设置cookie过期时间
            CookieUtils.setCookie(request,response,jwtConfig.getCookieName(),newToken,jwtConfig.getCookieMaxAge(),true);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            //解码错误
            return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }
}
