package com.izlei.shlibrary;

import android.content.Context;
import android.widget.Toast;

import com.com.izlei.app.AppController;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zhouzili on 2015/3/27.
 */
public class CreateBook {

    public static void addBook(final Book book) {
        book.save(AppController.getInstance(), new SaveListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(AppController.getInstance(),"添加数据成功，返回objectId为：" + book.getObjectId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(AppController.getInstance(), "创建数据失败：" + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
