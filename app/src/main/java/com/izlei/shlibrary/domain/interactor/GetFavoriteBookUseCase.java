package com.izlei.shlibrary.domain.interactor;

import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.exception.ErrorBundle;

import java.util.List;

/**
 * Created by zhouzili on 2015/5/18.
 */
public interface GetFavoriteBookUseCase extends Interactor {


    interface Callback {
        void onBookListLoaded(List<Book> bookList);
        void onError(ErrorBundle errorBundle);
    }

    void execute(Callback callback);

}
