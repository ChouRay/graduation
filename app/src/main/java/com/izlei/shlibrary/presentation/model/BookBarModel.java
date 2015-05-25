package com.izlei.shlibrary.presentation.model;

/**
 * Created by zhouzili on 2015/5/24.
 */
public class BookBarModel {
    UserModel userModel;
    String username;
    String leaveWords;
    String updatedAt;


    String commendWords;

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
    public UserModel getUserModel()
    {
        return this.userModel;
    }

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

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getUpdatedAt () {
        return this.updatedAt;
    }


    public void setCommendWords(String commendWords) {
        this.commendWords = commendWords;
    }
    public String getCommendWords() {
        return this.commendWords;
    }
}
