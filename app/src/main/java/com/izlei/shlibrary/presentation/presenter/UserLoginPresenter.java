package com.izlei.shlibrary.presentation.presenter;

import android.content.Context;

import com.izlei.shlibrary.domain.interactor.UserLoginOrSignUpUseCase;
import com.izlei.shlibrary.domain.interactor.UserLoginOrSignUpUseCaseImpl;
import com.izlei.shlibrary.presentation.mapper.UserModelDataMapper;
import com.izlei.shlibrary.presentation.model.UserModel;
import com.izlei.shlibrary.presentation.view.LoginSuccessListener;

/**
 * Created by zhouzili on 2015/5/7.
 */
public class UserLoginPresenter implements Presenter {

    private UserLoginOrSignUpUseCase userLoginUseCase;
    private UserModelDataMapper userModelDataMapper;

    private LoginSuccessListener listener;

    public UserLoginPresenter() {
        userModelDataMapper = new UserModelDataMapper();
    }

    public void setLoginListener(LoginSuccessListener listener) {
        this.listener = listener;
    }


    public void userLogin(Context context, String username,String psd) {
        UserModel userModel = new UserModel();
        userModel.setUsername(username);
        userModel.setPassword(psd);
        if (userLoginUseCase == null) {
            userLoginUseCase = new UserLoginOrSignUpUseCaseImpl();
        }
        userLoginUseCase.execute(userLoginCallback, context, userModelDataMapper.transform(userModel), 0);
        Thread thread = new Thread(userLoginUseCase);
        thread.start();
    }

    public void userSignUp(Context context, String username, String psd, String email) {
        UserModel userModel = new UserModel();
        userModel.setUsername(username);
        userModel.setPassword(psd);
        userModel.setEmail(email);
        if (userLoginUseCase == null) {
            userLoginUseCase = new UserLoginOrSignUpUseCaseImpl();
        }
        userLoginUseCase.execute(userLoginCallback, context, userModelDataMapper.transform(userModel), 1);
        Thread thread = new Thread(userLoginUseCase);
        thread.start();
    }

    private final UserLoginOrSignUpUseCase.UserLoginOrSignUpCallback userLoginCallback = new UserLoginOrSignUpUseCase.UserLoginOrSignUpCallback() {
        @Override
        public void onUserLoginOrSignUpSuccess(boolean param) {
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
