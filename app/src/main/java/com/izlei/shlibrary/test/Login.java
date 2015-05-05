package com.izlei.shlibrary.test;

import com.izlei.shlibrary.app.AppController;
import com.izlei.shlibrary.bean.User;
import com.izlei.shlibrary.utils.ToastUtil;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zhouzili on 2015/4/3.
 */
public class Login {

    public User login(User user,final String name, final String password) {
        user.setUsername(name);
        user.setPassword(password);
        user.login(AppController.getInstance(), new SaveListener() {
            @Override
            public void onSuccess() {
                ToastUtil.show("login success!");
            }
            @Override
            public void onFailure(int i, String s) {
                ToastUtil.show("Login failure! " + s);
            }
        });
        return user;
    }
}
