package com.izlei.shlibrary.demo.impl;

/**
 * 用于监听各个事件 比如：登录成功，加入收藏，加入下载等
 * Created by zhouzili on 2015/4/11.
 */
public interface IMyListener {
    public void onListenerMessage(Object obj, int flag);
}
