package com.fjut.util;


import com.fjut.pojo.Book;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;

//将分页功能的所有信息拼接到一个字符串中作为返回结果，直接传递到页面使用
public class PageUtil {
    /**
     * 拼接分页的方法
     * @param pageInfo 分页信息，用于判断分页的页码
     * @param path 点击要跳转的路径
     * @param params 需要携带的参数
     */
    public static String getPageInfo(PageInfo<? extends Object> pageInfo,String path,
                                    String params){
        //需要跳转的页面，以：  路径/的形式
        String page=path;

        //定义结果字符串
        StringBuilder stringBuilder=new StringBuilder();

        //拼接首页
        if(pageInfo.getPages()==0){
            stringBuilder.append("首页");
        }else{
            stringBuilder.append("<a href='"+page+"1"+params+"'>首页</a>");
        }
        //相当于空格
        stringBuilder.append("&nbsp;&nbsp;");

        //拼接上一页
        if(pageInfo.isHasPreviousPage()){
            stringBuilder.append("<a href='"+page+(pageInfo.getPageNum()-1)+params+"'"+">上一页</a>");
            stringBuilder.append("&nbsp;&nbsp;");
        }else{
            stringBuilder.append("上一页");
            stringBuilder.append("&nbsp;&nbsp;");
        }

        //拼接页码超链接
        int[] nums=pageInfo.getNavigatepageNums();
        for (int num : nums) {
            if(num== pageInfo.getPageNum()){
                stringBuilder.append("<a style='text-decoration:none;' href='"+page+num+params+"'>"+num+"</a>");
                stringBuilder.append("&nbsp;&nbsp;");
            }else{
                stringBuilder.append("<a href='"+page+num+params+"'>"+num+"</a>");
                stringBuilder.append("&nbsp;&nbsp;");
            }
        }

        //拼接下一页
        if(pageInfo.isHasNextPage()){
            stringBuilder.append("<a href='"+page+(pageInfo.getPageNum()+1)+params+"'"+">下一页</a>");
            stringBuilder.append("&nbsp;&nbsp;");
        }else{
            stringBuilder.append("下一页");
            stringBuilder.append("&nbsp;&nbsp;");
        }

        //拼接尾页
        if(pageInfo.getPages()==0){
            stringBuilder.append("尾页");
        }else{
            stringBuilder.append("<a href='"+page+pageInfo.getPages()+params+"'>尾页</a>");
        }
        return stringBuilder.toString();
    }

}