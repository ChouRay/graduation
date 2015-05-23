package com.izlei.shlibrary.presentation.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.izlei.shlibrary.R;
import com.izlei.shlibrary.presentation.model.UserModel;
import com.izlei.shlibrary.presentation.presenter.UserLoginPresenter;
import com.izlei.shlibrary.presentation.view.LoginSuccessListener;
import com.izlei.shlibrary.presentation.view.UIRefresh;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhouzili on 2015/5/9.
 */
public class SignUpFragment extends BaseFragment implements LoginSuccessListener {

    @InjectView(R.id.edit_name)
    EditText editTextName;

    @InjectView(R.id.edit_password)
    EditText editTextPsd;

    @InjectView(R.id.edit_email)
    EditText editTextEmail;

    private UserLoginPresenter userPresenter;
    private UserModel userModel;
    private UIRefresh uiRefresh;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.userPresenter = new UserLoginPresenter();
        uiRefresh = (UIRefresh) activity;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.userPresenter != null) {
            this.userPresenter.setLoginListener(this);
        }
    }

    @OnClick(R.id.button_ok)
    public void onSignUpClick() {
        this.userPresenter.userSignUp(getActivity(), editTextName.getText().toString(),
                editTextPsd.getText().toString(),
                editTextEmail.getText().toString());  //1 present sign_up command
    }


    private void  refreshUI (boolean isTrue) {
        if (isTrue) {
            this.uiRefresh.notifyUIRefresh();
        }else {
            this.showToastMessage("SignUp Failure!");
        }
    }


    @Override
    public void onLoginSuccess(boolean success) {
        refreshUI(success);
    }
}
