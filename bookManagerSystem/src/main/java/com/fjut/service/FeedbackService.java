package com.fjut.service;

import com.fjut.mapper.FeedbackMapper;

import com.fjut.pojo.Feedback;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeedbackService {
    @Autowired
    private  FeedbackMapper feedbackMapper;

    /**
     * 更新用户反馈，当管理员进行答复之后使用
     * @param responseInfo 答复信息
     * @param respId  答复的反馈id
     * @param responseTime 答复时间
     */
    public  boolean updateResInfo(String responseInfo, Integer respId, String responseTime){

        return feedbackMapper.updateResInfo(responseInfo,respId,responseTime);
    }

    /**
     * 通过关键值查询所有反馈
     * @param userId 用户id
     * @param feedbackInfo 查询信息
     * @param searchType 查询类型
     *                   username：表示通过用户名查询
     *                   information：表示通过反馈信息查询
     *                   noneHandle：表示查询未答复的反馈
     *                   其他：表示查询所有反馈
     * @param pageNum 页码
     */
    public Map<String,Object> queryAllFeedbackByKey(Integer userId, String feedbackInfo, String searchType, Integer pageNum){
        PageHelper.startPage(pageNum,6);
        List<Feedback> feedbacks;
        if(searchType.equals("username")){
            //pagehelper会将分页的limit加在开启分页后的第一个SQL查询语句上，所以查询用户id要放置开启分页之前。下方注释代码错误。
            //feedbacks = feedbackService.queryFeedbackByKey(userService.queryUserByName(feedbackInfo).getUserId(),null,null);
            feedbacks = feedbackMapper.queryFeedbackByKey(userId,null,null);
        }else if(searchType.equals("information")){
            feedbacks= feedbackMapper.queryFeedbackByKey(null,feedbackInfo,null);
        }else if(searchType.equals("noneHandle")){
            feedbacks= feedbackMapper.queryFeedbackByKey(null,null,0);
        }else{
            feedbacks= feedbackMapper.queryFeedbackByKey(null,null,null);
        }
        PageInfo<Feedback> pageInfo =new PageInfo<>(feedbacks,5);
        Map<String,Object> map=new HashMap<>();
        map.put("feedbacks",feedbacks);
        map.put("pageInfo",pageInfo);
        return map;
    }

    /**
     * 通过关键词查询当前用户反馈
     * @param userId 用户id
     * @param feedbackInfo 查询信息
     * @param searchType 查询类型
     *                   informationSearch：表示通过反馈信息查询
     *                   noneHandle：表示查询未答复的反馈
     *                   其他：表示查询未答复，且符合搜索信息的反馈
     * @param pageNum 页码
     * @return
     */
    public Map<String,Object> queryUserFeedbackByKey(Integer userId, String feedbackInfo, String searchType, Integer pageNum){
        PageHelper.startPage(pageNum,6);
        List<Feedback> feedbacks;
        if(searchType.equals("informationSearch")){
            //pagehelper会将分页的limit加在开启分页后的第一个SQL查询语句上，所以查询用户id要放置开启分页之前。下方注释代码错误。
            //feedbacks = feedbackService.queryFeedbackByKey(userService.queryUserByName(feedbackInfo).getUserId(),null,null);
            feedbacks = feedbackMapper.queryFeedbackByKey(userId,feedbackInfo,null);
        }else if(searchType.equals("noneHandle")){
            feedbacks= feedbackMapper.queryFeedbackByKey(userId,null,0);
        }else if(searchType.equals("noneHandleSearch")){
            feedbacks= feedbackMapper.queryFeedbackByKey(userId,feedbackInfo,0);
        }else {
            feedbacks=feedbackMapper.queryFeedbackByKey(userId,null,null);
        }
        PageInfo<Feedback> pageInfo =new PageInfo<>(feedbacks,5);
        Map<String,Object> map=new HashMap<>();
        map.put("feedbacks",feedbacks);
        map.put("pageInfo",pageInfo);
        return map;
    }

    /**
     * 提交反馈
     * @param feedback 反馈对象
     */
    public boolean submitFeedback(Feedback feedback){
        return feedbackMapper.submitFeedback(feedback);
    }
}