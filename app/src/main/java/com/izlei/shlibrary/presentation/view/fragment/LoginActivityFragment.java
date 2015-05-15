package com.izlei.shlibrary.presentation.view.fragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.izlei.shlibrary.R;
import com.izlei.shlibrary.presentation.model.UserModel;
import com.izlei.shlibrary.presentation.presenter.UserLoginPresenter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends BaseFragment implements LoginSuccessListener{

    @InjectView(R.id.edit_password)
    EditText editTextPsd;
    @InjectView(R.id.edit_name)
    EditText editTextName;

    private UIRefresh uiRefresh;
    private UserLoginPresenter userPresenter;

    public LoginActivityFragment() {
    }

    public static LoginActivityFragment newInstance() {
        return new LoginActivityFragment();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.userPresenter = new UserLoginPresenter();
        try {
            uiRefresh = (UIRefresh) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement UIRefresh");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.userPresenter != null) {
            this.userPresenter.setLoginListener(this);
        }
    }

    @OnClick(R.id.button_login)
    public void onLoginClick() {
        UserModel userModel = new UserModel();
        userModel.setUsername(editTextName.getText().toString());
        userModel.setPassword(editTextPsd.getText().toString());
        this.userPresenter.userLogin(getActivity(), userModel, 0);  //0 present login command
    }

    @OnClick(R.id.textView_sign_up)
    public void onSignUpClick() {
        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, SignUpFragment.newInstance());
        fragmentTransaction.commit();
    }

    private void  refreshUI (boolean isTrue) {
        if (isTrue) {
            this.uiRefresh.notifyUIRefresh();
        }
    }

    @Override
    public void onLoginSuccess(boolean success) {
        refreshUI(success);
    }
}
