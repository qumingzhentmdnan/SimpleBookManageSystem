package com.fjut.service;

import com.fjut.mapper.UserMapper;
import com.fjut.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(locations ="classpath:spring.xml")
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    //通过用户名查询用户
    public User queryUserByName(String username){
        User user = userMapper.queryUserByName(username);
        return user;
    }

    //添加用户
    public boolean addUser(User user){
        return userMapper.addUser(user);
    }

    //通过用户名和密码查询用户是否存在和匹配
    public boolean queryUser(String username,String password){
        return userMapper.queryUser(username,password)!=null;
    }
}