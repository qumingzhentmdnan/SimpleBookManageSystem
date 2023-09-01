package com.fjut.pojo;

import org.springframework.stereotype.Component;

@Component
public class User {
    private Integer userId;
    private String username;
    private String password;
    private Integer allowBorrowing;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(Integer userId, String username, String password, Integer allowBorrowing, String email) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.allowBorrowing = allowBorrowing;
        this.email = email;
    }

    public User() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAllowBorrowing() {
        return allowBorrowing;
    }

    public void setAllowBorrowing(Integer allowBorrowing) {
        this.allowBorrowing = allowBorrowing;
    }

}