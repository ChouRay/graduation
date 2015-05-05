package com.izlei.shlibrary.demo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.izlei.shlibrary.app.AppController;
import com.izlei.shlibrary.domain.Book;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zhouzili on 2015/3/27.
 */
public class CreateBook {

    public static final String TAG = AppController.class
            .getSimpleName();

    private Context context;

    public CreateBook(Context context) {
        this.context = context;
    }

    public void addBook(final Book book) {
        book.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Log.i(TAG, "添加数据成功，返回objectId为：" + book.getObjectId());
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                if (code != 401) {
                    Toast.makeText(AppController.getInstance(), "创建数据失败："+"code== "+code + msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
