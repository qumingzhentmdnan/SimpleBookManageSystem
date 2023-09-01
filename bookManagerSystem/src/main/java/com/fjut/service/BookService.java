package com.fjut.service;

import com.fjut.mapper.BookMapper;
import com.fjut.pojo.Book;
import com.fjut.pojo.BorrowingBook;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {
    @Autowired
    private BookMapper bookMapper;

    /**
     * 根据图书id查询图书
     */
    public Book queryBookById(Integer id){
        return bookMapper.queryBookById(id);
    }


    /**
     * 通过图书id删除图书
     */
    public boolean delBookById(Integer id){
        return bookMapper.delBookById(id);
    }

    /**
     * 添加一本图书
     */
    public boolean addBook(Book book){
        return bookMapper.addBook(book);
    }

    /**
     * 更新图书
     */
    public boolean updateBook(Book book){
        return bookMapper.updateBook(book);
    }

    /**
     * 通过关键词查询图书
     * @param bookInfo 图书信息，为name，author，isbn中的一种
     * @param searchType 为name时，查询书名与bookInfo相关的书籍
     *                   为author时，查询作者与bookInfo相关的书籍
     *                   为isbn时，查询isbn与bookInfo相关的书籍
     *                   为其他时，查询所有书籍
     * @param pageNum 需要查询的页面，用于分页
     */
    public Map<String,Object> queryBookByKeyword(String bookInfo,String searchType,int pageNum){
        PageHelper.startPage(pageNum,6);
        List<Book> books;

        if(searchType.equals("name")){
            books = bookMapper.queryBookByKeyword(bookInfo,null,null);
        }else if(searchType.equals("author")){
            books=bookMapper.queryBookByKeyword(null,bookInfo,null);
        }else if(searchType.equals("isbn")){
            books=bookMapper.queryBookByKeyword(null,null,bookInfo);
        }else{
            books=bookMapper.queryBookByKeyword(null,null,null);
        }

        PageInfo<Book> pageInfo =new PageInfo<>(books,5);
        Map<String,Object> map=new HashMap();
        map.put("pageInfo",pageInfo);
        map.put("books",books);
        return map;
    }

    /**
     * 遍历设置用户借阅的具体时间
     * @param borrowingBooks 查询到借阅集合
     */
    private void setDuration(List<BorrowingBook> borrowingBooks){
        for (BorrowingBook borrowingBook : borrowingBooks) {
            LocalDateTime date1 = LocalDateTime.now();
            LocalDateTime date2 = LocalDateTime.parse(borrowingBook.getDeadlineReturn(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Duration between = Duration.between(date1, date2);
            String duration=between.toDays()+"天  "+(between.toHours()-between.toDays()*24)+"时";
            borrowingBook.setTimeRemaining(duration.replace("-",""));//使结果为正
        }
    }
    /**
     * 通过关键词查询当前用户借阅的图书
     * @param Info 图书信息，为name，author，isbn中的一种
     * @param searchType 为name时，查询书名与Info相关的书籍
     *                   为order时，查询订单号与Info相关的书籍
     *                   为noReturn时，查询未归还的书籍
     *                   为其他时，查询所有书籍
     * @param id 当前用户id
     * @param pageNum 需要查询的页面，用于分页
     */
    public Map<String,Object> queryBorrowingBookByKey(String Info,String searchType,int pageNum,int id){
        PageHelper.startPage(pageNum,6);
        List<BorrowingBook> borrowingBooks;

        if(searchType.equals("name")){
            borrowingBooks = bookMapper.queryBorrowingBookByKey(Info,null,null,id);
        }else if(searchType.equals("order")){
            borrowingBooks=bookMapper.queryBorrowingBookByKey(null,Info,null,id);
        }else if(searchType.equals("noReturn")){
            borrowingBooks=bookMapper.queryBorrowingBookByKey(null,null,1,id);
        }else{
            borrowingBooks=bookMapper.queryBorrowingBookByKey(null,null,null,id);
        }
        this.setDuration(borrowingBooks);
        PageInfo<BorrowingBook> pageInfo =new PageInfo<>(borrowingBooks,5);

        Map<String,Object> map=new HashMap();
        map.put("pageInfo",pageInfo);
        map.put("borrowingBooks",borrowingBooks);
        return  map;
    }
}