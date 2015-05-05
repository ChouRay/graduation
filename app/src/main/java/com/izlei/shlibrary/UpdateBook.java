package com.izlei.shlibrary;

import android.content.Context;

import com.izlei.shlibrary.app.AppController;
import com.izlei.shlibrary.bean.Book;
import com.izlei.shlibrary.utils.ToastUtil;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zhouzili on 2015/3/27.
 */
public class UpdateBook {
    private Context context;
    public UpdateBook(Context context) {
        this.context = context;
    }

    public  void updateBookStock(Book book, int num) {
        book.setStock(book.getStock() + num);
        book.update(context, book.getObjectId(),new UpdateListener() {
            @Override
            public void onSuccess() {
                ToastUtil.show("操作成功");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.show("操作失败！s=="+s);
            }
        });
    }

}
