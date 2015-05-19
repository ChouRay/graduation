package com.izlei.shlibrary.presentation.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.exception.ErrorBundle;
import com.izlei.shlibrary.domain.interactor.GetFavoriteBookUseCase;
import com.izlei.shlibrary.domain.interactor.GetFavoriteBookUserCaseImpl;
import com.izlei.shlibrary.presentation.mapper.BookModelDataMapper;
import com.izlei.shlibrary.presentation.model.BookModel;
import com.izlei.shlibrary.presentation.view.BookListView;

import java.util.List;

/**
 * Created by zhouzili on 2015/5/18.
 */
public class FavoriteBookListPresenter implements Presenter {
    private BookListView bookListView;

    private final GetFavoriteBookUseCase getFavoriteBookUseCase;
    private final BookModelDataMapper bookModelDataMapper;

    public FavoriteBookListPresenter() {
        getFavoriteBookUseCase = new GetFavoriteBookUserCaseImpl();
        bookModelDataMapper = new BookModelDataMapper();
    }

    public void setView(@NonNull BookListView view) {
        this.bookListView = view;
    }

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
    public void onBookClicked(BookModel bookModel) {
        this.bookListView.viewBook(bookModel);
    }

    /**
     * Loads all books
     */
    public void loadBookList() {
        this.hideViewRetry();
        this.showViewLoading();
        this.getBookList();
    }

    private void getBookList()
    {
        this.showViewLoading();
        this.hideViewRetry();
        this.getFavoriteBookUseCase.execute(callback);

        Thread thread = new Thread(getFavoriteBookUseCase);
        thread.start();
    }

    private GetFavoriteBookUseCase.Callback callback = new GetFavoriteBookUseCase.Callback() {
        @Override
        public void onBookListLoaded(List<Book> bookList) {
            FavoriteBookListPresenter.this.hideViewLoading();
            FavoriteBookListPresenter.this.bookListView.renderBookList(bookModelDataMapper.transformFavorite(bookList));
        }

        @Override
        public void onError(ErrorBundle errorBundle) {
            FavoriteBookListPresenter.this.showViewRetry();
            FavoriteBookListPresenter.this.hideViewLoading();
            Log.e(FavoriteBookListPresenter.class.getSimpleName(), "error"+errorBundle.getErrorMessage());
        }
    } ;

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }
}
