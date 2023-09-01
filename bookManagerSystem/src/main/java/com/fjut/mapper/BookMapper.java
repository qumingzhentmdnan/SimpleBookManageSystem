package com.fjut.mapper;

import com.fjut.pojo.Book;
import com.fjut.pojo.BorrowingBook;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMapper {
    Book queryBookById(Integer id);
    boolean delBookById(Integer id);

    List<Book> queryBookByKeyword(@Param("name") String name, @Param("author")String author,@Param("isbn") String isbn);

    boolean addBook(Book book);

    boolean updateBook(Book book);

    List<BorrowingBook> queryBorrowingBookByKey(@Param("name") String name, @Param("sequence")String sequence,@Param("status") Integer status,@Param("userId") Integer userId);
}
