package com.fjut.controller;

import com.fjut.pojo.Feedback;
import com.fjut.pojo.User;
import com.fjut.service.FeedbackService;
import com.fjut.service.UserService;
import com.fjut.util.PageUtil;

import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Map;

@Controller
public class FeedbackServlet {

    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private UserService userService;
    /**
     * 当管理员答复时，更新数据
     * @param responseInfo 答复信息
     * @param respId 被答复人的id
     * @param num 发起更新的页码，答复完后跳回原页码
     * @return
     */
    @PostMapping("updateResponseInfor")
    public String updateInfor(String responseInfo,Integer respId,Integer num,HttpSession session){
        if(session.getAttribute("root")==null){
            return "redirect:/";
        }
        boolean b = feedbackService.updateResInfo(responseInfo, respId, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return "redirect:/toHandleFeedbackPage/"+num;
    }

    /**
     * 查询所有用户的所有反馈——管理员账号调用
     * @param pageNum 用于分页
     * @param map 用于将数据放入请求域中
     * @return
     */
    @RequestMapping("toHandleFeedbackPage/{pageNum}")
    public String searchAllFeedback(@PathVariable("pageNum") Integer pageNum, ModelMap map,HttpSession session){
        if(session.getAttribute("root")==null){
            return "redirect:/";
        }
        Map<String, Object> resultMap = feedbackService.queryAllFeedbackByKey(null,null,"all",pageNum);
        String pages = PageUtil.getPageInfo((PageInfo<? extends Object>) resultMap.get("pageInfo"), "toHandleFeedbackPage/","");
        map.put("feedbacks",resultMap.get("feedbacks"));
        map.put("pageInfo",pages);
        map.put("num",pageNum);
        return "user/handle_feedback";
    }

    /**
     * 查询符合条件的所有用户的所有反馈——管理员账号调用
     * @param pageNum 用于分页
     * @param feedbackInfo 拼接在分页超链接上，因为查询指定反馈之后，用户点击其他页码时不会再输入feedbackInfo，需要从这里获得
     * @param searchType 拼接在分页超链接上，因为查询指定反馈之后，用户点击其他页码时不会再输入searchType，需要从这里获得
     * @param map 将数据放入请求域
     * @return
     */
    @GetMapping("/searchFeedback/{pageNum}")
    public String SearchAllFeedbackByKey(@PathVariable("pageNum") Integer pageNum,String feedbackInfo,
                             String searchType,ModelMap map,HttpSession session){
        if(session.getAttribute("root")==null){
            return "redirect:/";
        }

        Integer id=null;
        //只有searchType为username时，才代表用户名搜索
        if(searchType.equals("username")){
            User res=userService.queryUserByName(feedbackInfo);
            //必须先判断搜索结果是否存在，避免空指针异常，存在再通过get方法得到id。不存在再赋值-1，是因为数据库中查询不到-1的值
            id=res==null?-1:res.getUserId();
        }

        Map<String, Object> resultMap = feedbackService.queryAllFeedbackByKey(id, feedbackInfo, searchType, pageNum);
        String pages = PageUtil.getPageInfo((PageInfo<? extends Object>) resultMap.get("pageInfo"),"searchFeedback/","?feedbackInfo="+feedbackInfo+"&searchType="+searchType);

        map.put("pageInfo",pages);
        map.put("feedbacks",resultMap.get("feedbacks"));
        map.put("num",pageNum);
        return "user/handle_feedback";
    }

    /**
     * 查询当前用户的所有反馈——普通账号调用
     * @param pageNum 用于分页
     * @param session 获得当前用户的id
     * @param map 将数据储存至请求域中
     * @return
     */
    @GetMapping("toFeedbackPage/{pageNum}")
    public String searchUserFeedback(@PathVariable("pageNum") Integer pageNum,HttpSession session,ModelMap map){
        if(session.getAttribute("root")==null){
            return "redirect:/";
        }
        Map<String, Object> resultMap = feedbackService.queryUserFeedbackByKey((Integer) session.getAttribute("userId"), null, "all", pageNum);
        String pages = PageUtil.getPageInfo((PageInfo<? extends Object>) resultMap.get("pageInfo"), "toFeedbackPage/","");
        map.put("feedbacks",resultMap.get("feedbacks"));
        map.put("pageInfo",pages);
        map.put("num",pageNum);
        return "user/feedback";
    }

    /**
     * 查询当前用户的符合搜索条件的反馈——普通账号调用
     * @param pageNum 用于分页
     * @param feedbackInfo 拼接在分页超链接上，因为查询指定反馈之后，用户点击其他页码时不会再输入feedbackInfo，需要从这里获得
     * @param searchType 拼接在分页超链接上，因为查询指定反馈之后，用户点击其他页码时不会再输入searchType，需要从这里获得
     * @param map 用于将数据放入请求域
     * @param session 获得当前用户的id
     * @return
     */
    @GetMapping("/searchSubmitFeedback/{pageNum}")
    public String searchUserFeedbackByKey(@PathVariable("pageNum") Integer pageNum,String feedbackInfo,
                             String searchType,ModelMap map,HttpSession session){
        if(session.getAttribute("root")==null){
            return "redirect:/";
        }

        Map<String, Object> resultMap = feedbackService.queryUserFeedbackByKey((Integer) session.getAttribute("userId"), feedbackInfo, searchType, pageNum);

        String pages = PageUtil.getPageInfo((PageInfo<? extends Object>) resultMap.get("pageInfo"),"searchSubmitFeedback/","?feedbackInfo="+feedbackInfo+"&searchType="+searchType);
        map.put("pageInfo",pages);
        map.put("feedbacks",resultMap.get("feedbacks"));
        map.put("num",pageNum);
        return "user/feedback";
    }

    /**
     * 跳转提交反馈页面
     * @return
     */
    @GetMapping("toSubmitFeedbackPage")
    public String toSubmitFeedbackPage(HttpSession session){
        if(session.getAttribute("root")==null){
            return "redirect:/";
        }
        return "user/submit_feedback";
    }

    /**
     * 提交反馈
     * @param feedbackType 反馈类型
     * @param email 联系方式
     * @param feedbackInfo 反馈信息
     * @param session 用于获取提交反馈信息的用户id
     * @return
     */
    @PostMapping("SubmitFeedback")
    public String SubmitFeedback(Integer feedbackType,String email,String feedbackInfo,HttpSession session){
        if(session.getAttribute("root")==null){
            return "redirect:/";
        }

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Feedback feedback = new Feedback(null, (Integer) session.getAttribute("userId"), feedbackType, time, feedbackInfo, 0, null, email, null);
        feedbackService.submitFeedback(feedback);
        return "redirect:/toFeedbackPage/1";
    }


}