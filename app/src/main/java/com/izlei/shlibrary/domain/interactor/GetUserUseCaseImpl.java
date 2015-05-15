package com.izlei.shlibrary.domain.interactor;

import android.content.Context;

import com.izlei.shlibrary.data.repository.UserDataRepository;
import com.izlei.shlibrary.domain.User;
import com.izlei.shlibrary.domain.executor.PostExecutionThread;
import com.izlei.shlibrary.domain.repository.UserRepository;
import com.izlei.shlibrary.presentation.UIThread;

import java.util.List;

/**
 * Created by zhouzili on 2015/5/14.
 */
public class GetUserUseCaseImpl implements GetUserUseCase {

    private Context context;
    private Callback callback;
    private UserRepository repository;
    private int flag;

    final private PostExecutionThread postExecutionThread;
    public GetUserUseCaseImpl(){
        repository = new UserDataRepository();
        postExecutionThread = new UIThread();
    }

    @Override
    public void execute(Callback callback, Context context, int flag) {
        this.callback = callback;
        this.context = context;
        this.flag = flag;
    }

    @Override
    public void run() {
        repository.getUser(getUserCallback, context, flag);
    }

    private UserRepository.GetUserCallback getUserCallback = new UserRepository.GetUserCallback() {
        @Override
        public void setCurrentUser(User user) {
            notifyGetCurrentUserSuccessfully(user);
        }

        @Override
        public void setRelationUsers(List<User> users) {
            notifyGetRelationUsersSuccessfully(users);
        }
    };


    private void notifyGetCurrentUserSuccessfully(final User user) {
        postExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onCurrentUser(user);
            }
        });
    }

    private void notifyGetRelationUsersSuccessfully(final List<User> users) {
        postExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onRelationUsers(users);
            }
        });
    }
}
