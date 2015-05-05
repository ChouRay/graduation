package com.izlei.shlibrary.domain.interactor;

import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.exception.ErrorBundle;

import java.util.List;

/**
 * This interface represents a execution unit for a use case to get a List of {@link com.izlei.shlibrary.domain.Book}.
 * By convention this use case (Interactor) implementation will return the result using a Callback.
 * That callback should be executed in the UI thread.
 *
 * Created by zhouzili on 2015/4/20.
 */
public interface GetBookListUseCase extends Interactor{

    /**
     * Callback used to be notified when either a books
     * list has been loaded or an error happened.
     */
    interface Callback {
        void onBookListLoaded(List<Book> bookList);
        void onError(ErrorBundle errorBundle);
    }

    /**
     * Executes this book case.
     *
     * @param callback A {@link GetBookListUseCase.Callback} used to notify the client.
     * @param paramInt
     */
    void execute(Callback callback, int paramInt, int flag);
}
