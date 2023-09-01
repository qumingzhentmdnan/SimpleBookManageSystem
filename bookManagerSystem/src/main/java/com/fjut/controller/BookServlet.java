package com.fjut.controller;

import com.fjut.pojo.Book;

import com.fjut.service.BookService;
import com.fjut.util.PageUtil;

import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

import java.util.Map;
import java.util.UUID;

@Controller
public class BookServlet {
    @Autowired
    private BookService bookService;

    /**
     * 重定向到国家图书数据中心
     */
    @GetMapping("/BookDataCenter")
    public String toBookDataCenter(HttpSession session){
        //如果当前不在登录状态，用户操作直接跳转登录页面
       if(session.getAttribute("root")==null){
            return "redirect:/";
        }
        return "redirect:http://www.nlc.cn/web/index.shtml";
    }


    /**
     * 转发至图书修改界面
     * @param id 通过id查询对应书籍
     * @param num 修改完书籍之后，需要返回到原先的页面，所以记住分页页码
     * @param url 修改页面的跳转可以从homepage和book_manage两个页面发起，所以跳转回原页面需要通过URL参数
     * @param map 将数据放入请求域中
     */
    @GetMapping("/toBookUpdate")
    public String toUpdateBook(Integer id,Integer num,String url,ModelMap map,HttpSession session){
       if(session.getAttribute("root")==null){
            return "redirect:/";
        }
        Book book = bookService.queryBookById(id);
        map.put("book",book);
        map.put("num",num);
        map.put("url",url);
        return "manager/book_edit";
    }

    /**
     * 转发至图书添加页面
     * @param num 添加完书籍之后，需要返回到原先的页面，所以记住分页页码
     * @param map  将数据放入请求域中
     */
    @GetMapping("/toBookAdd")
    public String toAddBook(Integer num,ModelMap map,HttpSession session){
       if(session.getAttribute("root")==null){
            return "redirect:/";
        }
        map.put("num",num);
        return "manager/book_add";
    }


    /**
     * 删除图书
     * @param id 要删除图书id
     * @param num 删除后返回的页码
     */
     @DeleteMapping("/book")
     public String deleteBook(Integer id,Integer num,HttpSession session){
         if(session.getAttribute("userId")==null){
             return "user/login";
         }
     bookService.delBookById(id);
     return "redirect:toBookManage/"+num;
     }

    /**
     *  用于处理用户提交的图片
     * @param session 获得真实路径，用于存放上传图片
     * @param uploadFile 用户上传非文件
     * @param book 原本图书信息，用于更改其封面属性
     */
    public  void submitPicture(HttpSession session,MultipartFile uploadFile , Book book){
        if(uploadFile.getSize()>0){//图片大小大于0，即存在上传图片
            //避免文件名长度过长，大于数据库设置储存容量100
            String olderName=uploadFile.getOriginalFilename();
            if(olderName.length()>30){
                olderName = olderName.substring(olderName.length() - 30);
            }

            String filename = UUID.randomUUID() + olderName;//随机数拼接上图片原先名字，成为新名
            String realPath = session.getServletContext().getRealPath("static/uploads/" + filename);//其真实储存地址
            try {
                if(book.getPicture()!="static/uploads/默认封面.png"){//如果原先图片不为空，就删除原先图片，针对修改操作
                    File file = new File(session.getServletContext().getRealPath("static/uploads/" + book.getPicture()));
                    file.delete();
                }
                uploadFile.transferTo(new File(realPath));
                book.setPicture("static/uploads/" + filename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 添加图书
     * @param uploadFile 管理员上传的书籍封面
     * @param book 书籍
     * @param num 添加后跳转页面
     * @param session 获取上下文对象，进而获取真实路径
     */
    @PostMapping("/book")
    public String addBook(MultipartFile uploadFile, Book book, Integer num, HttpSession session){
       if(session.getAttribute("root")==null){
            return "redirect:/";
        }
        this.submitPicture(session,uploadFile,book);
        boolean b = bookService.addBook(book);
        return "redirect:toBookManage/"+num;
    }

    /**
     * 更新书籍
     * @param uploadFile 管理员上传的书籍封面
     * @param book 书籍
     * @param num 添加后跳转页面
     * @param session 获取上下文对象，进而获取真实路径
     */
    @PostMapping("/updatebook")
    public String updateBook(MultipartFile uploadFile,Book book,Integer num, String url,HttpSession session){
       if(session.getAttribute("root")==null){
            return "redirect:/";
        }
        this.submitPicture(session,uploadFile,book);
        boolean b = bookService.updateBook(book);
        return "redirect:"+url+"/"+num;
    }

    /**
     * 查询所有书籍信息，url为toHomepage或toBookManage都将通过此方法，分页并查询到所有书籍的信息，然后渲染到对应的页面
     * @param map 用于将数据放置请求域
     * @param pageNum 跳转页码
     * @param req 用于获取URL
     */
    @RequestMapping(value = {"/toHomepage/{pageNum}","/toBookManage/{pageNum}"})
    public String searchAllBook(ModelMap map, @PathVariable("pageNum" ) Integer pageNum, HttpServletRequest req,HttpSession session){
       if(session.getAttribute("root")==null){
            return "redirect:/";
        }
        Map<String, Object> resultMap = bookService.queryBookByKeyword(null,"all",pageNum);//从service中查询的结果，包括书籍信息和分页信息，存储在map集合中
        String pages;//字符串形式的前端分页代码
        String url;//要跳转的页面
        if( req.getRequestURL().indexOf("toHomepage")!=-1){
            pages = PageUtil.getPageInfo((PageInfo)resultMap.get("pageInfo"), "toHomepage/","");
            url="user/homepage";
        }else{
            pages = PageUtil.getPageInfo((PageInfo)resultMap.get("pageInfo"), "toBookManage/","");
            url="manager/book_manager";
        }
        map.put("pageInfo",pages);
        map.put("books",resultMap.get("books"));
        map.put("num",pageNum);
        return url;
    }

    /**
     * 通过信息查询图书，根据传来的searchType不同，表示不同的搜索策略，书名、作者、isbn，bookInfo为其中一个的值，将他们传入bookService做进一步处理
     * @param pageNum 查询之后的结果同样存在分页，用于修改之后返回原先页码
     * @param bookInfo 查询信息
     * @param searchType 查询类型
     * @param map 将数据放入请求域
     */
    @GetMapping("/book/{pageNum}")
    public String SearchBookByKey(@PathVariable("pageNum") Integer pageNum,String bookInfo,
                             String searchType,ModelMap map,HttpSession session){
       if(session.getAttribute("root")==null){
            return "redirect:/";
        }
        Map<String, Object> resultMap = bookService.queryBookByKeyword(bookInfo, searchType, pageNum);
        String pages = PageUtil.getPageInfo((PageInfo<? extends Object>) resultMap.get("pageInfo"),"book/","?searchType="+searchType+"&bookInfo="+bookInfo);
        map.put("pageInfo",pages);
        map.put("books",resultMap.get("books"));
        map.put("num",pageNum);
        return "manager/book_manager";
    }

    /**
     * 查询当前用户借阅的所有书籍
     * @param pageNum 查询第几页
     * @param map 将查询结果放入请求域
     * @param session 得到当前用户id属性，登录时放入的域中
     */
    @GetMapping("/toBorrowingBook/{pageNum}")
    public String searchAllBorrowingBook(@PathVariable("pageNum") Integer pageNum, ModelMap map, HttpSession session){
        if(session.getAttribute("root")==null){
            return "redirect:/";
        }
        Map<String, Object> resultMap = bookService.queryBorrowingBookByKey("","all", pageNum,(Integer) session.getAttribute("userId"));
        String pages = PageUtil.getPageInfo((PageInfo<? extends Object>) resultMap.get("pageInfo"),"toBorrowingBook/","");
        map.put("pageInfo",pages);
        map.put("borrowingBooks",resultMap.get("borrowingBooks"));
        map.put("num",pageNum);
        return "manager/book_borrowing";
    }

    /**
     * 查询当前用户借阅的指定书籍
     * @param pageNum 要跳转的页面
     * @param Info 查询书籍的信息
     * @param searchType 查询类型
     * @param map 将查询结果放入请求域中
     * @param session 得到当前用户id属性，登录时放入的域中
     */
    @GetMapping("/searchBorrowingBook/{pageNum}")
    public String searchBorrowingBookByKey(@PathVariable("pageNum") Integer pageNum,String Info,
                             String searchType,ModelMap map,HttpSession session){
        if(session.getAttribute("root")==null){
            return "redirect:/";
        }
        Map<String, Object> resultMap = bookService.queryBorrowingBookByKey(Info, searchType, pageNum,(Integer) session.getAttribute("userId"));
        String pages = PageUtil.getPageInfo((PageInfo<? extends Object>) resultMap.get("pageInfo"), "searchBorrowingBook/","?searchType="+searchType+"&Info="+Info);
        map.put("pageInfo",pages);
        map.put("borrowingBooks",resultMap.get("borrowingBooks"));
        map.put("num",pageNum);
        return "manager/book_borrowing";
    }
}