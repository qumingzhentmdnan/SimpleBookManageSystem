package com.fjut.mapper;

import com.fjut.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    User queryUserByName(String username);
    boolean addUser(User user);
    User queryUser(@Param("username") String username, @Param("password") String password);
}
