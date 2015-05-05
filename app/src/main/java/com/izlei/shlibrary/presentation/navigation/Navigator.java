package com.izlei.shlibrary.presentation.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.izlei.shlibrary.presentation.view.activity.BookDetailsActivity;
import com.izlei.shlibrary.presentation.view.activity.BookListActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by zhouzili on 2015/4/19.
 */
@Singleton
public class Navigator {

    @Inject
    public Navigator() {}

    /**
     * Goes to book list screen
     * @param context A Context needed to open the destiny activity
     */
    public void navigationToBookList(Context context) {
        if (context != null) {
            Intent intentToLaunch = BookListActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigationToBookDetails(Context context, String isbn) {
        if (context != null) {
            Intent intentToLaunch = BookDetailsActivity.getCallingIntent(context);
            intentToLaunch.putExtra("ISBN", isbn);
            context.startActivity(intentToLaunch);
        }
    }
}
