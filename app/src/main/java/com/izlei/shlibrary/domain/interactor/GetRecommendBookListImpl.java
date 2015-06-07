package com.izlei.shlibrary.domain.interactor;

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
 * Created by zhouzili on 2015/6/4.
 */
public class GetRecommendBookListImpl implements GetBookListUseCase {

    private final BookRepository bookRepository;
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;
    private Callback callback;
    private int skipInt;
    private int flag;

    public GetRecommendBookListImpl() {
        bookRepository = new BookDataRepository();
        threadExecutor = JobExecutor.getInstance();
        postExecutionThread = new UIThread();
    }
    @Override
    public void execute(Callback callback, int paramInt, int flag) {
        if (callback == null) {
            throw new IllegalArgumentException("Interactor callback cannot be null!");
        }
        this.callback = callback;
        this.skipInt = paramInt;
        this.flag = flag;
        this.threadExecutor.execute(this);
    }

    @Override
    public void execute(Callback callback, String text) {

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

    @Override
    public void run() {
        this.bookRepository.getRecommendBookList(repositoryCallback);
    }

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
