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

import javax.inject.Inject;

/**
 * Created by zhouzili on 2015/4/20.
 */
public class GetBookListUseCaseImpl implements GetBookListUseCase {
    public static final String TAG = GetBookListUseCaseImpl.class.getSimpleName();

    private final BookRepository bookRepository;
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;

    private Callback callback;
    private int skipInt;
    private int flag;

    public GetBookListUseCaseImpl() {
        bookRepository = new BookDataRepository();
        threadExecutor = new JobExecutor();
        postExecutionThread = new UIThread();
    }

    /**
     * Constructor of the class.
     * @param bookRepository A {@link BookRepository} as a source for retrieving data.
     * @param threadExecutor {@link ThreadExecutor} used to execute this use case in a background
     * @param postExecutionThread {@link PostExecutionThread} used to post updates when the use case
     * has been executed.
     */
    @Inject
    public GetBookListUseCaseImpl(BookRepository bookRepository,
                                  ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread) {
        this.bookRepository = bookRepository;
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;

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
    public void run() {
        this.bookRepository.getBookList(this.repositoryCallback, this.skipInt, this.flag);
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
