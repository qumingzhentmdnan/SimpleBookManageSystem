package com.fjut.pojo;

public class BorrowingBook {
    private Integer orderId;
    private Integer userId;
    private Integer bookId;
    private Integer status;
    private String sequence;
    private String borrowingDate;
    private String deadlineReturn;
    private String timeRemaining;
    private String bookName;


    @Override
    public String toString() {
        return "BorrowingBook{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", bookId=" + bookId +
                ", status=" + status +
                ", sequence='" + sequence + '\'' +
                ", borrowingDate='" + borrowingDate + '\'' +
                ", deadlineReturn='" + deadlineReturn + '\'' +
                ", timeRemaining='" + timeRemaining + '\'' +
                '}';
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getBorrowingDate() {
        return borrowingDate;
    }

    public void setBorrowingDate(String borrowingDate) {
        this.borrowingDate = borrowingDate;
    }

    public String getDeadlineReturn() {
        return deadlineReturn;
    }

    public void setDeadlineReturn(String deadlineReturn) {
        this.deadlineReturn = deadlineReturn;
    }

    public String getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(String timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public BorrowingBook() {
    }
}