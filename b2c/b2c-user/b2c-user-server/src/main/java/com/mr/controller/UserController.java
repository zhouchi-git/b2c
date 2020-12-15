package com.mr.controller;

import com.mr.pojo.User;
import com.mr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController<query> {
    @Autowired
    private UserService userService;
//    GET /check/{data}/{type}

    /**
     * 校验用户名与手机号是否存在
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> check(@PathVariable("data") String data,@PathVariable("type") Integer type){
//        200：校验成功
//        400：参数有误
//        500：服务器内部异常
        if(data == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Boolean flag =null;
        try {
            flag = userService.check(data,type);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  ResponseEntity.ok(flag);
    }
//    POST /register
    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("register")
    public  ResponseEntity<Void> register(User user){
        //加正则判断如果不对就返回 400
        try {
            userService.register(user);
        }catch (Exception e){
            new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 根据参数中的用户名和密码查询指定用户
     * @param user
     * @return
     */
//    GET /query
    @PostMapping("query")
    public  ResponseEntity<Map<String, Object>> queryUser(@RequestBody User user){
        //加正则判断如果不对就返回 400
        Map<String, Object> map = null;
        try {
             map = userService.queryUser(user);
        }catch (Exception e){
            new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  ResponseEntity.ok(map);
    }
}
