package com.izlei.shlibrary.presentation.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.exception.ErrorBundle;
import com.izlei.shlibrary.domain.interactor.GetBookListUseCase;
import com.izlei.shlibrary.domain.interactor.GetRecommendBookListImpl;
import com.izlei.shlibrary.presentation.mapper.BookModelDataMapper;
import com.izlei.shlibrary.presentation.model.BookModel;
import com.izlei.shlibrary.presentation.view.BookListView;

import java.util.List;

/**
 * Created by zhouzili on 2015/6/4.
 */
public class RecommendBookListPresenter implements Presenter {
    public final static String TAG = RecommendBookListPresenter.class.getSimpleName();
    private BookListView bookListView;
    private final GetBookListUseCase getBookListUseCase;
    private final BookModelDataMapper bookModelDataMapper;


    public RecommendBookListPresenter() {
        getBookListUseCase = new GetRecommendBookListImpl();
        bookModelDataMapper = new BookModelDataMapper();
    }

    public void setView(@NonNull BookListView view) {
        this.bookListView = view;
    }

    /**
     * Loads all books
     */
    public void loadBookList(int paramInt, int flag) {
        this.hideViewRetry();
        this.showViewLoading();
        this.getBookList(paramInt, flag);
    }

    private void getBookList(int paramInt, int flag) {
        this.showViewLoading();
        this.getBookListUseCase.execute(bookListCallback, paramInt, flag);
    }

    private void showBooksListInView(List<Book> booksList) {
        final List<BookModel> bookModelsList = this.bookModelDataMapper.transform(booksList);
        this.bookListView.renderBookList(bookModelsList);   ///happened in BookListFragment.renderBookList
    }

    private GetBookListUseCase.Callback bookListCallback = new
            GetBookListUseCase.Callback() {
                @Override
                public void onBookListLoaded(List<Book> bookList) {
                    RecommendBookListPresenter.this.hideViewLoading();
                    RecommendBookListPresenter.this.showBooksListInView(bookList);
                }

                @Override
                public void onError(ErrorBundle errorBundle) {
                    RecommendBookListPresenter.this.hideViewLoading();
                    //BookListPresenter.this.showErrorMessage();
                    RecommendBookListPresenter.this.showViewRetry();
                    Log.e(TAG, errorBundle.getErrorMessage());
                }
            };


    private void hideViewRetry() {
        this.bookListView.hideRetry();
    }

    private void showViewRetry() {
        this.bookListView.showRetry();
    }

    private void showViewLoading() {
        this.bookListView.showLoading();
    }
    private void hideViewLoading() {
        this.bookListView.hideLoading();
    }
    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }
}
