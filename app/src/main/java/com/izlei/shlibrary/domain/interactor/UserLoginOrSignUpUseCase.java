package com.izlei.shlibrary.domain.interactor;

import android.content.Context;

import com.izlei.shlibrary.domain.User;

/**
 * this interface, i am lazy to mix up user login and sign_up together,DON'T BE DO LIKE THAT
 * in actual project.
 * By convention this use case (Interactor) implementation will return a bool value use a callback.
 *
 * Created by zhouzili on 2015/5/14.
 */
public interface UserLoginOrSignUpUseCase extends Interactor{

    interface UserLoginOrSignUpCallback {
        void onUserLoginOrSignUpSuccess(boolean param);
    }

    /**
     *
     * @param callback
     * @param context
     * @param user
     * @param flag {flag ==0 represent Login, flag = 1 represent Sign Up}
     */
    void execute(UserLoginOrSignUpCallback callback, Context context, User user, int flag);
}
