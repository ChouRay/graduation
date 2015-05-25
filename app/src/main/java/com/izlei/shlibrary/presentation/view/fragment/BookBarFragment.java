package com.izlei.shlibrary.presentation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.izlei.shlibrary.R;
import com.izlei.shlibrary.presentation.model.BookBarModel;
import com.izlei.shlibrary.presentation.model.UserModel;
import com.izlei.shlibrary.presentation.presenter.BookBarPresenter;
import com.izlei.shlibrary.presentation.view.LoadBookBarView;
import com.izlei.shlibrary.presentation.view.adapter.BookBarAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

/**
 * Created by zhouzili on 2015/5/24.
 */
public class BookBarFragment extends BaseFragment implements LoadBookBarView{

    BookBarPresenter bookBarPresenter;
    @InjectView(R.id.rv_leave_words)
    RecyclerView recyclerView;
    @InjectView(R.id.edit_send_text)
    EditText editTextLeaveWords;
    @InjectView(R.id.rl_retry)
    RelativeLayout rl_retry;
    @InjectView(R.id.rl_progress) RelativeLayout rl_progress;


    private LinearLayoutManager linearLayoutManager;
    private BookBarAdapter bookBarAdapter;

    public static BookBarFragment newInstance() {
        return new BookBarFragment();
    }

    public BookBarFragment() {
        super();
    }

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        bookBarPresenter = new BookBarPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bookbar, container, false);
        ButterKnife.inject(this, rootView);
        this.setupUI();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
        this.loadBookBarMoments();
    }

    private void initialize() {
        bookBarPresenter.setView(this);
    }

    private void loadBookBarMoments() {
        bookBarPresenter.loadCommentList();
    }

    private void setupUI() {
        this.recyclerView.setHasFixedSize(true);
        this.linearLayoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(linearLayoutManager);
        if (this.bookBarAdapter == null && getActivity() != null) {
            this.bookBarAdapter = new BookBarAdapter(getContext());
            recyclerView.setAdapter(bookBarAdapter);
        }
    }

    @OnClick(R.id.btn_send)
    public void onSendLeaveWords() {
        String leaveWords = editTextLeaveWords.getText().toString();
        String username=null;
        if (BmobUser.getCurrentUser(getActivity()) != null) {
            username = BmobUser.getCurrentUser(getActivity()).getUsername();
        }
        this.bookBarPresenter.createComment(leaveWords,username);
        editTextLeaveWords.setText("");
    }

    @Override
    public void rendererMoments(List<BookBarModel> bookBarModelList) {
        if (bookBarModelList != null) {
            if (this.bookBarAdapter != null) {
                bookBarAdapter.setMoments(bookBarModelList);
            }
        }
    }

    @Override
    public void rendererMoment(BookBarModel bookBarModel) {
        if (bookBarModel != null && bookBarAdapter != null) {
            bookBarAdapter.addMoments(bookBarModel);
        }
    }



    @OnClick(R.id.bt_retry)
    void onButtonRetryClick() {
        this.loadBookBarMoments();
    }


    @Override
    public void showLoading() {
        this.rl_progress.setVisibility(View.VISIBLE);
        if (getActivity() != null) {
            this.getActivity().setProgressBarIndeterminateVisibility(true);
        }
    }

    @Override
    public void hideLoading() {
        this.rl_progress.setVisibility(View.GONE);
        if (getActivity() != null) {
            this.getActivity().setProgressBarIndeterminateVisibility(false);
        }
    }

    @Override
    public void showRetry() {
        this.rl_retry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetry() {
        this.rl_retry.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override
    public Context getContext() {
        return this.getActivity().getApplicationContext();
    }
}
