package com.izlei.shlibrary.domain.interactor;

import android.util.Log;

import com.izlei.shlibrary.data.executor.JobExecutor;
import com.izlei.shlibrary.data.repository.BookDataRepository;
import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.exception.ErrorBundle;
import com.izlei.shlibrary.domain.executor.PostExecutionThread;
import com.izlei.shlibrary.domain.executor.ThreadExecutor;
import com.izlei.shlibrary.domain.repository.BookRepository;
import com.izlei.shlibrary.presentation.UIThread;

import java.util.List;

/**
 * Created by zhouzili on 2015/5/18.
 */
public class GetFavoriteBookUserCaseImpl implements GetFavoriteBookUseCase {
    private final BookRepository bookRepository;
    //private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;
    private GetFavoriteBookUseCase.Callback callback;

    private ThreadExecutor threadExecutor;

    public GetFavoriteBookUserCaseImpl() {
        bookRepository = new BookDataRepository();
        threadExecutor = JobExecutor.getInstance();
        postExecutionThread = new UIThread();
    }

    @Override
    public void execute(Callback callback) {
        this.callback = callback;
        threadExecutor.execute(this);
    }

    @Override
    public void run() {
        Log.e(getClass().getSimpleName(), Thread.currentThread().getName() + " Start");
        this.bookRepository.getFavoriteBookList(repositoryCallback);
        Log.e(getClass().getSimpleName(), Thread.currentThread().getName() + " End");
    }

    private final BookRepository.BookListCallback repositoryCallback =
            new BookRepository.BookListCallback() {
                @Override
                public void onBookListLoaded(List<Book> bookList) {
                    notifyGetBookListSuccessfully(bookList);
                }

                @Override
                public void onError(ErrorBundle errorBundle) {
                    notifyError(errorBundle);
                }
            };

    private void notifyGetBookListSuccessfully(final List<Book> bookList) {

        this.postExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onBookListLoaded(bookList);
            }
        });
    }
    private void notifyError(final ErrorBundle errorBundle) {
        this.postExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(errorBundle);
            }
        });
    }

}
