package com.izlei.shlibrary.domain.repository;

import android.content.Context;

import com.izlei.shlibrary.domain.User;

import java.util.List;

/**
 * Created by zhouzili on 2015/5/7.
 */
public interface UserRepository {
    interface UserLoginListener {
        void onLoginSuccessListener(boolean param);
    }
    void listenUserLogin(UserLoginListener listener,Context context,User user, int flag);

    interface GetUserCallback{
        void setCurrentUser(User user);
        void setRelationUsers(List<User> users);
    }

    void getUser(GetUserCallback userCallback, Context context, int flag);
}
