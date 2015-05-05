package com.izlei.shlibrary.presentation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.izlei.shlibrary.domain.executor.PostExecutionThread;



/**
 * Created by zhouzili on 2015/4/23.
 */
public class UIThread implements PostExecutionThread {
    public static final String TAG = UIThread.class.getSimpleName();
    private final Handler handler;

    public UIThread() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        this.handler.post(runnable);
    }
}
