package com.izlei.shlibrary.presentation.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.exception.ErrorBundle;
import com.izlei.shlibrary.domain.interactor.GetBookListUseCase;
import com.izlei.shlibrary.domain.interactor.GetBookListUseCaseImpl;
import com.izlei.shlibrary.presentation.mapper.BookModelDataMapper;
import com.izlei.shlibrary.presentation.model.BookModel;
import com.izlei.shlibrary.presentation.view.BookListView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhouzili on 2015/4/20.
 */
public class BookListPresenter  implements Presenter {

    public final static String TAG = "BookListPresenter.class";
    private BookListView bookListView;

    private final GetBookListUseCase getBookListUseCase;
    private final BookModelDataMapper bookModelDataMapper;


    public BookListPresenter() {
        getBookListUseCase = new GetBookListUseCaseImpl();
        bookModelDataMapper = new BookModelDataMapper();
    }

    @Inject
    public BookListPresenter(GetBookListUseCase getBookListUseCase,
                             BookModelDataMapper bookModelDataMapper) {
        this.getBookListUseCase = getBookListUseCase;
        this.bookModelDataMapper = bookModelDataMapper;
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

    public void loadSearchBookList(String text) {
        this.showViewLoading();
        this.getBookListUseCase.execute(bookListCallback,text);
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

    private void showBooksListInView(List<Book> booksList) {
        final List<BookModel> bookModelsList = this.bookModelDataMapper.transform(booksList);
        this.bookListView.renderBookList(bookModelsList);   ///happened in BookListFragment.renderBookList
    }

    private void getBookList(int paramInt, int flag) {
        this.showViewLoading();
        this.getBookListUseCase.execute(bookListCallback, paramInt, flag);
        //Thread thread = new Thread(this.getBookListUseCase);
        //thread.start();
    }
    private GetBookListUseCase.Callback bookListCallback = new
            GetBookListUseCase.Callback() {
                @Override
                public void onBookListLoaded(List<Book> bookList) {
                    BookListPresenter.this.hideViewLoading();
                    BookListPresenter.this.showBooksListInView(bookList);
                }

                @Override
                public void onError(ErrorBundle errorBundle) {
                    BookListPresenter.this.hideViewLoading();
                    //BookListPresenter.this.showErrorMessage();
                    BookListPresenter.this.showViewRetry();
                    Log.e(TAG, errorBundle.getErrorMessage());
                }
            };



    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }
}
