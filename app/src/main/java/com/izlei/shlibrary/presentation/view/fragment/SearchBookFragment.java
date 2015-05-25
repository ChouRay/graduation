package com.izlei.shlibrary.presentation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.izlei.shlibrary.R;
import com.izlei.shlibrary.presentation.model.BookModel;
import com.izlei.shlibrary.presentation.navigation.Navigator;
import com.izlei.shlibrary.presentation.presenter.BookListPresenter;
import com.izlei.shlibrary.presentation.view.BookListView;
import com.izlei.shlibrary.presentation.view.adapter.BookLayoutManager;
import com.izlei.shlibrary.presentation.view.adapter.BooksAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhouzili on 2015/5/19.
 */
public class SearchBookFragment extends BaseFragment implements SearchView.OnQueryTextListener, BookListView{

    @InjectView(R.id.sv_search_book)
    SearchView searchView;
    @InjectView(R.id.books_rv)
    RecyclerView rv_books;
    @InjectView(R.id.rl_retry)
    RelativeLayout rl_retry;
    @InjectView(R.id.rl_progress) RelativeLayout rl_progress;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private BookLayoutManager bookLayoutManager;
    private BooksAdapter booksAdapter;
    private BookListListener bookListListener;

    Navigator navigator;
    BookListPresenter bookListPresenter;

    public static SearchBookFragment newInstance() {
        return new SearchBookFragment();
    }

    /**
     * Interface for listening book list events.
     */
    public interface BookListListener {
        void onBookListListener(final BookModel bookModel);
    }

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BookListListener) {
            this.bookListListener = (BookListListener) activity;
        }
        this.bookListPresenter = new BookListPresenter();
        navigator = new Navigator();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.inject(this, rootView);
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        setupUI();
        return rootView;
    }
    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
    }

    private void setupUI() {
        this.rv_books.setHasFixedSize(true);
        this.bookLayoutManager = new BookLayoutManager(getActivity());
        this.rv_books.setLayoutManager(this.bookLayoutManager);
    }

    private void initialize() {
        this.bookListPresenter.setView(this);
    }
    private void LoadBookList(String text) {
        this.bookListPresenter.loadSearchBookList(text);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.LoadBookList(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public void renderBookList(List<BookModel> booksList) {
        if (booksList != null) {
            if (this.booksAdapter == null) {
                this.booksAdapter = new BooksAdapter(getActivity());
                this.booksAdapter.setOnItemClickListener(onItemClickListener);
                this.rv_books.setAdapter(this.booksAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
            SearchBookFragment.this.booksAdapter.setBookList(booksList);
        }
    }

    private BooksAdapter.OnItemClickListener onItemClickListener =
            new BooksAdapter.OnItemClickListener() {
                @Override
                public void onBookItemClickListener(BookModel bookModel) {
                    navigator.navigationToBookDetails(getActivity(), bookModel.getIsbn13());
                   /* if (BookListFragment.this.bookListPresenter != null &&
                            bookModel != null) {
                        BookListFragment.this.bookListPresenter.onBookClicked(bookModel);
                    }*/
                }
            };

    @Override
    public void onResume() {
        super.onResume();
        this.bookListPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.bookListPresenter.pause();
    }

    @Override
    public void viewBook(BookModel bookModel) {
        if (this.bookListListener != null) {
            this.bookListListener.onBookListListener(bookModel);
        }
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
