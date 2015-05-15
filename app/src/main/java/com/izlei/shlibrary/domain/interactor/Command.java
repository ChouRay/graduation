package com.izlei.shlibrary.domain.interactor;

import android.content.Context;

import com.izlei.shlibrary.domain.Book;

/**
 * Created by zhouzili on 2015/5/6.
 */
public interface Command extends Interactor{
    void execute(Context context, Object object);
}
