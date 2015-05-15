package com.izlei.shlibrary.data.repository;

import android.content.Context;

import com.izlei.shlibrary.data.entity.UserEntity;
import com.izlei.shlibrary.data.entity.mapper.UserEntityDataMapper;
import com.izlei.shlibrary.domain.User;
import com.izlei.shlibrary.domain.repository.UserRepository;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zhouzili on 2015/5/7.
 */
public class UserDataRepository implements UserRepository{
    public final static int LOGIN = 0;
    public final static int SIGNUP = 1;
    public final static int CURRENT_USER_COMMAND = 0;
    public final static int RELATION_USERS_COMMAND = 1;
    private UserLoginListener listener;
    private final UserEntityDataMapper userEntityDataMapper;
    public UserDataRepository() {
        this.userEntityDataMapper = new UserEntityDataMapper();
    }

    @Override
    public void listenUserLogin(UserLoginListener listener, Context context,User user, int flag) {
        this.listener = listener;

        if (flag == LOGIN) {
            this.login(context,userEntityDataMapper.transform(user));
        }
        if (flag == SIGNUP) {
            this.signUp(context, userEntityDataMapper.transform(user));
        }
    }

    private void signUp(final Context context,final UserEntity userEntity) {
        if(userEntity !=null) {
            userEntity.signUp(context, new SaveListener() {
                @Override
                public void onSuccess() {
                    listener.onLoginSuccessListener(true);
                }

                @Override
                public void onFailure(int i, String s) {
                    listener.onLoginSuccessListener(false);
                }
            });
        }
    }


    private void login(final Context context, final UserEntity userEntity) {
        if (userEntity != null) {
            userEntity.login(context, new SaveListener() {
                @Override
                public void onSuccess() {
                    listener.onLoginSuccessListener(true);
                }

                @Override
                public void onFailure(int i, String s) {
                    listener.onLoginSuccessListener(false);
                }
            });
        }
    }

    @Override
    public void getUser(GetUserCallback userCallback, Context context, int flag) {
        if (flag == CURRENT_USER_COMMAND) {
            findCurrentUser(context, userCallback);
        }
        if (flag == RELATION_USERS_COMMAND) {
            findRelationUsers(context,userCallback);
        }
    }

    private void findCurrentUser(Context context, final GetUserCallback callback) {
        UserEntity userEntity = BmobUser.getCurrentUser(context, UserEntity.class);
        callback.setCurrentUser(userEntityDataMapper.transform(userEntity));
    }
    private void findRelationUsers(Context context, final GetUserCallback callback) {
        BmobQuery<UserEntity> query = new BmobQuery<>();
        query.findObjects(context, new FindListener<UserEntity>() {
            @Override
            public void onSuccess(List<UserEntity> list) {
                callback.setRelationUsers(userEntityDataMapper.transform(list));
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


}
