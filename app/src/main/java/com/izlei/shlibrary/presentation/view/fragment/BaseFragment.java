package com.izlei.shlibrary.presentation.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by zhouzili on 2015/4/20.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * Show a {@link android.widget.Toast} message.
     * @param message message An string representing a message to be shown.
     */
    protected void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
