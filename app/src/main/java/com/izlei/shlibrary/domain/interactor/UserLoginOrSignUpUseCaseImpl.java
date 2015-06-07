package com.izlei.shlibrary.domain.interactor;

import android.content.Context;

import com.izlei.shlibrary.data.executor.JobExecutor;
import com.izlei.shlibrary.data.repository.UserDataRepository;
import com.izlei.shlibrary.domain.User;
import com.izlei.shlibrary.domain.executor.PostExecutionThread;
import com.izlei.shlibrary.domain.executor.ThreadExecutor;
import com.izlei.shlibrary.domain.repository.UserRepository;
import com.izlei.shlibrary.presentation.UIThread;

/**
 * Created by zhouzili on 2015/5/7.
 */
public class UserLoginOrSignUpUseCaseImpl implements UserLoginOrSignUpUseCase {

    private final PostExecutionThread postExecutionThread;
    private UserRepository userRepository;
    private final ThreadExecutor threadExecutor;
    private Context context;
    private User user;
    /**flag is login or signup*/
    private int flag;

    private UserLoginOrSignUpCallback callback;
    public UserLoginOrSignUpUseCaseImpl() {
        userRepository = new UserDataRepository();
        threadExecutor = JobExecutor.getInstance();
        postExecutionThread = new UIThread();
    }

    @Override
    public void execute(UserLoginOrSignUpCallback callback, Context context, User user, int flag) {
        this.callback = callback;
        this.context = context;
        this.user = user;
        this.flag = flag;
        threadExecutor.execute(this);
    }

    @Override
    public void run() {
        userRepository.listenUserLogin(repositorySignUpCallback, context, user,flag);
    }

    private final UserRepository.UserLoginListener repositorySignUpCallback  =
            new UserRepository.UserLoginListener() {
        @Override
        public void onLoginSuccessListener(boolean param) {
            notifyUiSignUpSuccessfully(param);
        }
    };

    private void notifyUiSignUpSuccessfully(final boolean success) {
        this.postExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onUserLoginOrSignUpSuccess(success);
            }
        });
    }
}
