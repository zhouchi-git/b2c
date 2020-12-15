package com.mr.service;

import com.mr.bo.UserInfo;
import com.mr.client.UserClient;
import com.mr.config.JwtConfig;
import com.mr.pojo.User;
import com.mr.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {
    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 登录验证
     * @param user
     * @return
     */
    public String  login(User user) throws Exception {
        Map<String, Object> map = userClient.queryUser(user);
        if(map != null){
               return JwtUtils.generateToken(new UserInfo(Long.parseLong(map.get("id").toString()), map.get("username").toString()), jwtConfig.getPrivateKey(), jwtConfig.getExpire());
        }
        return  null;
    }

    public void verify() {
    }
}
