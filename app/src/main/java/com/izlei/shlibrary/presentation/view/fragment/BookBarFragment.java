package com.izlei.shlibrary.presentation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.izlei.shlibrary.R;
import com.izlei.shlibrary.presentation.model.BookBarModel;
import com.izlei.shlibrary.presentation.presenter.BookBarPresenter;
import com.izlei.shlibrary.presentation.view.LoadBookBarView;
import com.izlei.shlibrary.presentation.view.adapter.BookBarAdapter;

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

    private int previousTotal = 0; // The total number of items in the data set after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 1; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

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
        if(savedInstanceState == null) {
            this.initialize();
            if (this.bookBarAdapter == null && getActivity() != null) {
                this.bookBarAdapter = new BookBarAdapter(getContext());
                recyclerView.setAdapter(bookBarAdapter);
            }
            this.loadBookBarMoments(0);
        }
    }

    private void initialize() {
        bookBarPresenter.setView(this);
    }

    private void loadBookBarMoments(int skip) {
        bookBarPresenter.loadCommentList(skip);
    }

    private void setupUI() {
        this.recyclerView.setHasFixedSize(true);
        this.linearLayoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.autoLoadedMore();
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


    private void autoLoadedMore() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                //Log.e(TAG, "visibleItemCount="+visibleItemCount+" totalItemCount="+totalItemCount+" firstVisibleItem"+firstVisibleItem+" previousTotal=="+previousTotal);
                if (totalItemCount < previousTotal) {
                    previousTotal = totalItemCount;
                }
                if (loading) {
                    if (totalItemCount > previousTotal + 1) {
                        loading = false;
                        previousTotal = totalItemCount;
                        //Log.e(TAG, "loading--previousTotal=="+previousTotal);
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached
                    BookBarFragment.this.loadBookBarMoments(bookBarAdapter.getItemCount());
                    loading = true;
                    Log.e(getClass().getSimpleName(), "not loading");
                }
            }
        });
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
            bookBarAdapter.addMoment(bookBarModel);
        }
    }



    @OnClick(R.id.bt_retry)
    void onButtonRetryClick() {
        this.loadBookBarMoments(0);
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
