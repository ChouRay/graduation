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
    }
}
