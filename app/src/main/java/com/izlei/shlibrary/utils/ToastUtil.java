package com.izlei.shlibrary.utils;

import android.widget.Toast;

import com.com.izlei.app.AppController;

/**
 * Created by zhouzili on 2015/3/27.
 */
public class ToastUtil {
    public static void show(String info)
    {
        Toast.makeText(AppController.getInstance(), "" + info, Toast.LENGTH_SHORT).show();
    }
}
