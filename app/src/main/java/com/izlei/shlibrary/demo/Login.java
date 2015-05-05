package com.izlei.shlibrary.demo;


import com.izlei.shlibrary.demo.impl.IMyListener;
import com.izlei.shlibrary.app.AppController;
import com.izlei.shlibrary.domain.User;
import com.izlei.shlibrary.utils.ToastUtil;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zhouzili on 2015/4/3.
 */
public class Login {

    public final static int LOGIN_SUCCESS = 10;
    public final static int LOGIN_FAILURE = 11;
    private IMyListener listener;

    public Login() {

    }

    public void setListener(IMyListener listener) {
        this.listener = listener;
    }

    public void login(final String name, final String password) {
        final User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        user.login(AppController.getInstance(), new SaveListener() {
            @Override
            public void onSuccess() {
                ToastUtil.show("login success!");
                if (listener != null) {
                    listener.onListenerMessage(user, Login.LOGIN_SUCCESS);
                }
            }
            @Override
            public void onFailure(int i, String s) {
                ToastUtil.show("Login failure! " + s);
            }
        });
    }
}
