package com.izlei.shlibrary;

import com.com.izlei.app.AppController;
import com.izlei.shlibrary.utils.ToastUtil;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zhouzili on 2015/3/27.
 */
public class UpdateBook {

    public static void updateBookStock(final Book book) {

        book.update(AppController.getInstance(), book.getObjectId(),new UpdateListener() {
            @Override
            public void onSuccess() {
                ToastUtil.show("操作成功");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.show("操作失败！"+s);
            }
        });
    }

}
