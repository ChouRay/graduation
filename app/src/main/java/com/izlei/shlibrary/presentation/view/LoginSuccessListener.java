package com.izlei.shlibrary.presentation.view;

import com.izlei.shlibrary.presentation.view.fragment.LoginActivityFragment;
import com.izlei.shlibrary.presentation.view.fragment.SignUpFragment;

/**
 * this interface represents a listener to listen for user login or sign up successful in {@link LoginActivityFragment}
 * and {@link SignUpFragment} in order to notify UI to refresh
 * Created by zhouzili on 2015/5/14.
 */
public interface LoginSuccessListener {
    void onLoginSuccess(boolean success);
}
