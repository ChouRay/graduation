package com.izlei.shlibrary.data.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by zhouzili on 2015/5/7.
 */
public class UserEntity extends BmobUser implements Serializable {

    String address;
    BmobFile avatar;
    BmobRelation currentBorrowRelation;

    public BmobRelation getCurrentBorrowRelation() {
        return this.currentBorrowRelation;
    }
    public void setCurrentBorrowRelation(BmobRelation currentBorrowRelation) {
        this.currentBorrowRelation = currentBorrowRelation;
    }

    public void setAddress(String address) {this.address = address;}
    public String getAddress() {return this.address;}
    public void setAvatar (BmobFile avatar) {this.avatar = avatar;}
    public BmobFile getAvatar() {return this.avatar;}
}
