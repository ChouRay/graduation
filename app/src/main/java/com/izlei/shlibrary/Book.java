package com.izlei.shlibrary;

import cn.bmob.v3.BmobObject;

public class Book extends BmobObject{
	String site;
	String title;
	String original_price;
	String selling_price;
	String pictureURL;
	String author;
	String publisher;
	String publish_date;
	String url;
    int stock = 0;
    String isbn;

    public Book(String site, String title, String original_price, String selling_price,
                String pictureURL, String author, String publisher, String publish_date,
                String url) {
        this.site = site;
        this.title = title;
        this.original_price = original_price;
        this.selling_price = selling_price;
        this.pictureURL = pictureURL;
        this.author = author;
        this.publisher = publisher;
        this.publish_date = publish_date;
        this.url = url;
    }

    public void setSite(String site) {this.site = site;}
    public String getSite() {
        return this.site;
    }
    public void setTitle(String title) {this.title = title;}
    public String getTitle () {
        return this.title;
    }
    public void setOriginal_price (String original_price) {this.original_price = original_price;}
    public String getOriginal_price() {
        return this.original_price;
    }
    public void setSelling_price (String selling_price) {this.selling_price = selling_price;}
    public String getSelling_price () {
        return this.selling_price;
    }
    public void setAuthor (String author) {this.author = author;}
    public String getAuthor () {
        return this.author;
    }
    public void setPictureURL (String pictureURL) {this.pictureURL = pictureURL;}
    public String getPictureURL() {
        return this.pictureURL;
    }
    public void setPublisher (String publisher) {this.publisher = publisher;}
    public String getPublisher () {
        return this.publisher;
    }
    public void setPublish_date(String publish_date) {this.publish_date = publish_date;}
    public String getPublish_date () {
        return publish_date;
    }
    public void setUrl (String url) {this.url = url;}
    public String getUrl () {return this.url;}

    public void setStock(int num) {
        this.stock += num;
    }
    public int getStock() {
        return this.stock;
    }

    public void setIsbn(String isbn) {this.isbn = isbn;}
    public String getIsbn() {return this.isbn;}

}
