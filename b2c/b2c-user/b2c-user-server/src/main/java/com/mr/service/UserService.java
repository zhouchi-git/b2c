package com.mr.service;

import com.mr.mapper.UserMapper;
import com.mr.pojo.User;
import com.mr.util.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 校验用户名与手机号是否存在
     * @param data
     * @param type
     * @return
     */
    public Boolean check(String data, Integer type) {
        User user = new User();
        if(type == null || type == 1){
            user.setUsername(data);
        }else {
            user.setPhone(data);
        }
        return  userMapper.selectCount(user) == 0;
    }

    /**
     * 用户注册
     * @param user
     */
    public void register(User user) {
        user.setCreated(new Date());
        user.setSalt(Md5Utils.generateSalt());
        user.setPassword(Md5Utils.md5Hex(user.getPassword(),user.getSalt()));
        userMapper.insert(user);
    }

    /**
     * 根据参数中的用户名和密码查询指定用户
     * @param user
     * @return
     */
    public Map<String, Object> queryUser(User user) {
        String password = user.getPassword();
        user.setPassword(null);
        User queryUser = userMapper.selectOne(user);
        if(queryUser != null){
            if(Md5Utils.md5Hex(password,queryUser.getSalt()).equals(queryUser.getPassword())){
                Map<String, Object> map = new HashMap<>();
                map.put("id",queryUser.getId());
                map.put("username",queryUser.getUsername());
                map.put("phone",queryUser.getPhone());
                map.put("created",queryUser.getCreated());
                return  map;
            }
        }
        return null;
    }
}
