package com.mr.api;

import com.mr.pojo.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

public interface UserApi {

    /**
     * 校验用户名与手机号是否存在
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    Boolean check(@PathVariable("data") String data, @PathVariable("type") Integer type);
//    POST /register
    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("register")
    Void register(User user);

    /**
     * 根据参数中的用户名和密码查询指定用户
     * @param user
     * @return
     */
//    GET /query
    @PostMapping("query")
    Map<String, Object> queryUser(@RequestBody User user);

}
