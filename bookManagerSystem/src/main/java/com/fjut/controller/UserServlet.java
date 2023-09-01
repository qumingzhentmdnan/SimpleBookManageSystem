package com.fjut.controller;

import com.fjut.pojo.User;
import com.fjut.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.function.BiConsumer;

@Controller
@SpringJUnitConfig(locations ="classpath:spring.xml")
public class UserServlet {
    @Autowired
    private UserService userService;

    /**
     * 转发至注册页面
     */
    @GetMapping("/regist")
    public String  toRegist(HttpSession session){
        return "user/regist";
    }

    /**
     * 配合regist页面的axios发送的异步请求，进行判断用户名是否重复
     * @return 返回错误信息结果，如果不重复就将错误信息置为空，反之添加“用户名重复”的错误信息
     */
    @PostMapping("/checkUsername")
    @ResponseBody
    public String checkUsername(String username){
        boolean res = userService.queryUserByName(username)!=null;
        String str="";
        if(res){
            str="用户名重复";
        }
        return str;
    }

    /**
     * 配合regist页面的axios发送的异步请求，判断验证码是否正确
     * @param verificationCode  用户在界面输入的验证码
     * @param session  验证码jar包-kaptcha，前端通过KaptchaServlet会访问其servlet，生成验证码图片
     *                 生成验证码后，会将其值储存在session域中，key值为KAPTCHA_SESSION_KEY，
     * @return 返回错误信息结果，如果验证码正确就将错误信息置为空，反之添加“验证码错误”的错误信息
     */
    @PostMapping("/checkerrVerificationCode")
    @ResponseBody
    public String checkerrVerificationCode(String verificationCode, HttpSession session){
        boolean judge = ((String) session.getAttribute("KAPTCHA_SESSION_KEY")).equals(verificationCode);
        String res="";
        if(!judge){
            res="验证码不正确";
        }
        return res;
    }


    /**
     * 当用户注册成功之后，将其数据添加到数据库中
     * @param username 用户名
     * @param password 用户密码
     * @param email 邮箱
     * @return 重定向至登录页面
     */
    @PostMapping("/addUser")
    public String addUser(String username,String password,String email){
        //后端判断注册账号是否合规
        if(username.matches("^[a-zA-Z0-9]{6,16}$")&&password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])[A-Za-z0-9]{6,10}$")
                &&email.matches("^[a-zA-Z0-9_\\.-]+@([a-zA-Z0-9-]+[\\.]{1})+[a-zA-Z]+$")&&userService.queryUserByName(username)==null){
            User user = new User(0,username, password, 20,email);
            userService.addUser(user);
            return "redirect:/";
        }else{
            return "redirect:/regist";
        }
    }

    /**
     * 当进入登录页面，若是用户端存在账号密码相关cookie，将其输入到登录框，做数据的回显
     * @param username 用户名
     * @param password 密码
     * @param map 将数据放入请求域
     * @return /转发至登录页面
     */
    @RequestMapping(value = {"/"})
    public String toIndex(@CookieValue(value = "username",required = false) String username,
                          @CookieValue(value = "password",required = false) String password,
                          Map<String,String> map){
        if(password!=null&&username!=null){
            map.put("username",username);
            map.put("password",password);
            map.put("errMessage","");
        }
        return "user/login";
    }

    /**
     * 当用户在登录界面进行登录，进行检查登录信息
     * @param username 用户名
     * @param password 密码
     * @param rep 用于在Cookie添加账号密码
     * @param session 用于在session中添加登录状态root,以及当前登录用户的id
     */
    @PostMapping("/checkLogin")
    @ResponseBody
    public String checkLogin (String username, String password, HttpServletResponse rep,HttpSession session){
        boolean res = userService.queryUser(username, password);

        String key;
        if(res){
            //创建一个消费型函数接口，接受字符创s1，s2分别为cookie的key和value,将其创建并添加到请求域中
            BiConsumer<String, String> stringStringBiConsumer = (String s1, String s2) -> {
                Cookie cookie = new Cookie(s1, s2);
                cookie.setMaxAge(60 * 60 * 24 * 7);
                rep.addCookie(cookie);
            };
            stringStringBiConsumer.accept("username",username);
            stringStringBiConsumer.accept("password",password);

            session.setMaxInactiveInterval(60*60*24*7);
            session.setAttribute("userId",userService.queryUserByName(username).getUserId());
            if(username.contains("@")){
                //表示为管理员权限，前端页面中禁止含有@的账号注册，只能在数据库中直接添加
                session.setAttribute("root",1);
            }else{
                //表示为普通用户权限
                session.setAttribute("root",0);
            }
            //返回登录结果，注意，axios会自动将字符串true，false转换为boolean类型，在js中就会出现"true"!=key……
            key="true";
        }else{
            key="false";
        }
        return key;
    }

    /**
     * 当用户进行退出时，抹去密码的cookie，返回到登录页面
     * @param username 原cookie中的用户名
     * @param map 将数据放入请求域
     * @param resp 用于创建密码的同名cookie，置为空，使其不会在展示到登录界面的密码框中
     * @session session 用于删除session中的root，即清除当前登录状态，避免用户不在登录状态，还可以访问除登录外其他页面
     */
    @GetMapping("/quit")
    public String quit(@CookieValue(value = "username",required = false) String username,
                          Map<String,String> map,HttpServletResponse resp,HttpSession session){
        Cookie cookie = new Cookie("password", "");
        resp.addCookie(cookie);
        session.removeAttribute("root");
        map.put("username",username);
        map.put("errMessage","请输入密码");
        return "user/login";
    }

}