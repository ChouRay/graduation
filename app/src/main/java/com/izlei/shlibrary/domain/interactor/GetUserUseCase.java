package com.izlei.shlibrary.domain.interactor;

import android.content.Context;

import com.izlei.shlibrary.domain.User;

import java.util.List;

/**
 * Created by zhouzili on 2015/5/14.
 */
public interface GetUserUseCase extends Interactor{

    interface Callback{
        void onCurrentUser(User user);
        void onRelationUsers(List<User> users);
        void onError();
    }

    void execute(Callback callback, Context context, int flag);

}
