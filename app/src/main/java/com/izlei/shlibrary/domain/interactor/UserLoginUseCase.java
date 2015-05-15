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

    void execute(UserLoginCallback callback, Context context, User user, int flag);
}
