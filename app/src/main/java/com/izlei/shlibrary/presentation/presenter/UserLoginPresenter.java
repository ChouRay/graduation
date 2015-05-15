package com.izlei.shlibrary.presentation.presenter;

import android.content.Context;

import com.izlei.shlibrary.domain.interactor.UserLoginUseCase;
import com.izlei.shlibrary.domain.interactor.UserLoginUseCaseImpl;
import com.izlei.shlibrary.presentation.mapper.UserModelDataMapper;
import com.izlei.shlibrary.presentation.model.UserModel;
import com.izlei.shlibrary.presentation.view.fragment.LoginSuccessListener;

/**
 * Created by zhouzili on 2015/5/7.
 */
public class UserLoginPresenter implements Presenter {

    private UserLoginUseCase userLoginUseCase;
    private UserModelDataMapper userModelDataMapper;

    private LoginSuccessListener listener;

    public UserLoginPresenter() {
        userModelDataMapper = new UserModelDataMapper();
    }

    public void setLoginListener(LoginSuccessListener listener) {
        this.listener = listener;
    }


    public void userLogin(Context context, UserModel userModel, int flag) {
        if (userLoginUseCase == null) {
            userLoginUseCase = new UserLoginUseCaseImpl();
        }
        userLoginUseCase.execute(userLoginCallback, context, userModelDataMapper.transform(userModel), flag);
        Thread thread = new Thread(userLoginUseCase);
        thread.start();
    }

    private final UserLoginUseCase.UserLoginCallback userLoginCallback = new UserLoginUseCase.UserLoginCallback() {
        @Override
        public void onUserSignUpSuccess(boolean param) {
            listener.onLoginSuccess(param);
        }
    };



    @Override
    public void resume() {
        
    }

    @Override
    public void pause() {

    }
}
