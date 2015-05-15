package com.izlei.shlibrary.domain;

import com.izlei.shlibrary.data.entity.UserEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhouzili on 2015/4/3.
 */
public class CurrentBorrow extends BmobObject{
    String title;
    String isbn;
    String borrowDate;
    String sendbackDate;
    UserEntity user;

    public void setUser(UserEntity user) {
        this.user = user;
    }
    public UserEntity getUser() {
        return this.user;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    public void setIsbn (String isbn) {
        this.isbn = isbn;
    }
    public String getIsbn() {
        return this.isbn;
    }

    public void setBorrowDate(long timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
        Date curDate = new Date(timestamp);//获取当前时间
        this.borrowDate =  formatter.format(curDate);
    }
    public String getBorrowDate() {
        return this.borrowDate;
    }
    public void setSendbackDate (long timestamp, int day) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.add(Calendar.DATE, day);
        Date date = new Date(calendar.getTimeInMillis());
        this.sendbackDate = format.format(date);
    }
    public String getSendbackDate() {
        return this.sendbackDate;
    }

}
