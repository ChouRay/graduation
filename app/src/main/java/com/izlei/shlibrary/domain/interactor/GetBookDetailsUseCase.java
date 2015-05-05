package com.izlei.shlibrary.domain.interactor;

import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.exception.ErrorBundle;

/**
 * Created by zhouzili on 2015/4/28.
 */
public interface GetBookDetailsUseCase extends Interactor {

    interface Callback {
        public void onBookDataLoaded(Book book);
        public void onError(ErrorBundle errorBundle);
    }

    /***
     * Executes this book case.
     * @param isbn the isbn of retrieve book.
     * @param callback A {@link Callback} used for notify the clients.
     */
    public void execute(String isbn, Callback callback);

}
