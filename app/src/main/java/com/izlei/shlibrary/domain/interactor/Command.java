package com.izlei.shlibrary.domain.interactor;

import android.content.Context;

import com.izlei.shlibrary.domain.Book;

/**
 * this interface represents a about Command pattern for an execution unit for a series of actions like
 * donating book, sending book,returning book so on.
 *
 * Created by zhouzili on 2015/5/6.
 */
public interface Command extends Interactor{
    void execute(Context context, Object object);
}
