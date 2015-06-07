package com.izlei.shlibrary.presentation.presenter;

import android.content.Context;

import com.izlei.shlibrary.domain.User;
import com.izlei.shlibrary.domain.interactor.GetUserUseCase;
import com.izlei.shlibrary.domain.interactor.GetUserUseCaseImpl;
import com.izlei.shlibrary.presentation.mapper.UserModelDataMapper;
import com.izlei.shlibrary.presentation.model.UserModel;
import com.izlei.shlibrary.utils.ToastUtil;

import java.util.List;

/**
 * Created by zhouzili on 2015/5/15.
 */
public class GetUserPresenter implements Presenter{
    private final GetUserUseCase userUseCase;
    private final UserModelDataMapper userModelDataMapper;
    private GetUserPresenterCallback getUserPresenterCallback;

    public GetUserPresenter(){
        userModelDataMapper = new UserModelDataMapper();
        userUseCase = new GetUserUseCaseImpl();
    }

    public void setUserPresenterCallback(GetUserPresenterCallback getUserPresenterCallback){
        this.getUserPresenterCallback = getUserPresenterCallback;
    }

    public void getCurrentUser(Context context) {
        userUseCase.execute(callback, context,0);
    }
    public void getRelationUsers(Context context) {
        userUseCase.execute(callback, context, 1);
    }

    private GetUserUseCase.Callback callback = new GetUserUseCase.Callback() {
        @Override
        public void onCurrentUser(User user) {
            getUserPresenterCallback.getCurrentUser(userModelDataMapper.transform(user));
        }

        @Override
        public void onRelationUsers(List<User> users) {
            getUserPresenterCallback.getRelationUsers(userModelDataMapper.transform(users));
        }

        @Override
        public void onError() {

        }
    };


    public interface GetUserPresenterCallback {
        void getCurrentUser(UserModel userModel);
        void getRelationUsers(List<UserModel> userModels);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }
}
