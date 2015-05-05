package com.izlei.shlibrary.domain;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by zhouzili on 2015/4/2.
 */
public class User extends BmobUser implements Serializable{
    public static final long serialVersionUID = -12345678933009L;
    String adress;
    BmobFile avatar;
    BmobRelation currentBorrow;

    public BmobRelation getCurrentBorrow() {
        return this.currentBorrow;
    }
    public void setCurrentBorrow(BmobRelation currentBorrow) {
        this.currentBorrow = currentBorrow;
    }


    public void setAdress(String adress) {this.adress = adress;}
    public String getAdress() {return this.adress;}
    public void setAvatar (BmobFile avatar) {this.avatar = avatar;}
    public BmobFile getAvatar() {return this.avatar;}
}
