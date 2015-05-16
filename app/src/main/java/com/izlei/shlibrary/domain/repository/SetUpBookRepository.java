package com.izlei.shlibrary.domain.repository;

import android.content.Context;

import com.izlei.shlibrary.domain.Book;

/**
 * Created by zhouzili on 2015/5/6.
 */
public interface SetUpBookRepository {
    void createBook(Context context, Book book);
    void borrowBook(Context context, Book book);
    void sendBackBook(Context context, Book book);
}
