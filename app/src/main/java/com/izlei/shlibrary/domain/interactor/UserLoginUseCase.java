package com.izlei.shlibrary.domain.interactor;

import android.content.Context;

import com.izlei.shlibrary.domain.User;

/**
 * Created by zhouzili on 2015/5/14.
 */
public interface UserLoginUseCase extends Interactor{
    interface UserLoginCallback {
        void onUserSignUpSuccess(boolean param);
    }

    /**
     *
     * @param callback
     * @param context
     * @param user
     * @param flag {flag ==0 represent Login, flag = 1 represent Sign Up}
     */
    void execute(UserLoginCallback callback, Context context, User user, int flag);
}
