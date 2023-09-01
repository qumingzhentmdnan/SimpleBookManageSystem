package com.fjut.pojo;

import org.springframework.stereotype.Component;

@Component
public class Book {
    private Integer bookId;
    private String name;
    private String author;
    private Integer lending;
    private Integer remain;
    private String picture;
    private String isbn;

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", lending=" + lending +
                ", remain=" + remain +
                ", picture='" + picture + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }

    public Book() {
    }

    public Book(Integer bookId, String name, String author, Integer lending, Integer remain, String picture, String isbn, String address) {
        this.bookId = bookId;
        this.name = name;
        this.author = author;
        this.lending = lending;
        this.remain = remain;
        this.picture = picture;
        this.isbn = isbn;
        this.address = address;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getLending() {
        return lending;
    }

    public void setLending(Integer lending) {
        this.lending = lending;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}