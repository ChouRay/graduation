package com.izlei.shlibrary.domain.interactor;

import com.izlei.shlibrary.data.executor.JobExecutor;
import com.izlei.shlibrary.data.repository.BookDataRepository;
import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.exception.ErrorBundle;
import com.izlei.shlibrary.domain.executor.PostExecutionThread;
import com.izlei.shlibrary.domain.executor.ThreadExecutor;
import com.izlei.shlibrary.domain.repository.BookRepository;
import com.izlei.shlibrary.presentation.UIThread;

/**
 * Created by zhouzili on 2015/4/28.
 */
public class GetBookDetailsUseCaseImpl implements GetBookDetailsUseCase {
    private final BookRepository bookRepository;
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;

    private Callback callback;
    String isbn = "";

    public GetBookDetailsUseCaseImpl () {
        bookRepository = new BookDataRepository();
        threadExecutor = new JobExecutor();
        postExecutionThread = new UIThread();
    }

    @Override
    public void execute(String isbn, Callback callback) {
        if (isbn.isEmpty() || callback == null) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        this.callback = callback;
        this.isbn = isbn;
        this.threadExecutor.execute(this);
    }

    @Override
    public void run() {
        this.bookRepository.getBookDetails(isbn,repositoryCallback);
    }

    private final BookRepository.BookDetailsCallback repositoryCallback =
            new BookRepository.BookDetailsCallback() {
                @Override
                public void onBookDetailsLoaded(Book book) {
                    notifyGetBookDetailsSuccessfully(book);
                }

                @Override
                public void onError(ErrorBundle errorBundle) {
                    notifyError(errorBundle);
                }
            };

    private void notifyGetBookDetailsSuccessfully(final Book book) {
        this.postExecutionThread.post(
                new Runnable() {
                    @Override
                    public void run() {
                        callback.onBookDataLoaded(book);
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
