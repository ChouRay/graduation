package com.izlei.shlibrary.data.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhouzili on 2015/5/24.
 */
public class BookBarEntity extends BmobObject {
    String username;
    String leaveWords;
    String commendWords;

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return this.username;
    }

    public void setLeaveWords(String leaveWords) {
        this.leaveWords = leaveWords;
    }
    public String getLeaveWords() {
        return this.leaveWords;
    }



    public void setCommendWords(String commendWords) {
        this.commendWords = commendWords;
    }
    public String getCommendWords() {
        return this.commendWords;
    }
}
