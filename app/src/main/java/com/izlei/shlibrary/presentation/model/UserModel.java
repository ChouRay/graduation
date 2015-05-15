package com.izlei.shlibrary.presentation.model;

import java.io.Serializable;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by zhouzili on 2015/5/7.
 */
public class UserModel implements Serializable{
    String address;
    BmobFile avatar;
    BmobRelation currentBorrow;
    String username;

    String password;
    String email;

    public BmobRelation getCurrentBorrow() {
        return this.currentBorrow;
    }
    public void setCurrentBorrow(BmobRelation currentBorrow) {
        this.currentBorrow = currentBorrow;
    }

    public void setUsername(String username) {this.username = username;}
    public String getUsername() {return this.username;}

    public void setPassword(String password) {this.password = password;}
    public String getPassword() {return this.password;}

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return this.email;
    }

    public void setAddress(String address) {this.address = address;}
    public String getAddress() {return this.address;}
    public void setAvatar (BmobFile avatar) {this.avatar = avatar;}
    public BmobFile getAvatar() {return this.avatar;}
}
