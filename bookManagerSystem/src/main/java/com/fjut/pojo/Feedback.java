package com.fjut.pojo;

import org.springframework.stereotype.Component;

@Component
public class Feedback {
    private Integer id;
    private Integer userId;
    private Integer type;
    private String username;
    private String date;

    public Feedback(Integer id, Integer userId, Integer type, String date, String feedbackInfo, Integer status, String responseInfo, String email, String responseTime) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.date = date;
        this.feedbackInfo = feedbackInfo;
        this.status = status;
        this.responseInfo = responseInfo;
        this.email = email;
        this.responseTime = responseTime;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", userId=" + userId +
                ", type=" + type +
                ", username='" + username + '\'' +
                ", date='" + date + '\'' +
                ", feedbackInfo='" + feedbackInfo + '\'' +
                ", status=" + status +
                ", responseInfo='" + responseInfo + '\'' +
                ", email='" + email + '\'' +
                ", responseTime='" + responseTime + '\'' +
                '}';
    }

    private String feedbackInfo;
    private Integer status;
    private String responseInfo;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String responseTime;

    public String getDate() {
        return date;
    }

    public Integer getId() {
        return id;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public Feedback() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFeedbackInfo() {
        return feedbackInfo;
    }

    public void setFeedbackInfo(String feedbackInfo) {
        this.feedbackInfo = feedbackInfo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(String responseInfo) {
        this.responseInfo = responseInfo;
    }
}