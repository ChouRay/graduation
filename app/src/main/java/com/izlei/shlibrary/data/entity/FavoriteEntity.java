package com.izlei.shlibrary.data.entity;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhouzili on 2015/5/18.
 */
public class FavoriteEntity extends BmobObject{
    private String isbn13;
    private String image;
    private String title;
    private String price;
    UserEntity user;


    public void setUserEntity(UserEntity user) {
        this.user = user;
    }
    public UserEntity getUserEntity() {
        return this.user;
    }

    public void setImage (String image) {
        this.image = image;
    }
    public String getImage() {
        return this.image;
    }
    public void setTitle (String title) {
        this.title =title;
    }
    public String getTitle () {
        return this.title;
    }
    public void setIsbn13 (String isbn13) {
        this.isbn13 = isbn13;
    }
    public String getIsbn13() {
        return this.isbn13;
    }
    public void setPrice (String price) {
        this.price = price;
    }
    public String getPrice () {
        return this.price;
    }

}
