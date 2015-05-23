package com.izlei.shlibrary.domain.repository;

import com.izlei.shlibrary.data.repository.datasource.BookDataStore;
import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.exception.ErrorBundle;

import java.util.List;

/**
 * Interface that represents Repository for getting {@link com.izlei.shlibrary.domain.Book}
 * related data
 * Created by zhouzili on 2015/4/20.
 */
public interface BookRepository {

    /**
     * Callback used to be notified when either a book list has been loaded or error happened.
     */
    interface BookListCallback {
        void onBookListLoaded(List<Book> bookList);
        void onError(ErrorBundle errorBundle);
    }

    interface BookDetailsCallback {
        void onBookDetailsLoaded(Book book);
        void onError(ErrorBundle errorBundle);
    }

    /**
     * Get a List of {@link Book}
     * @param bookListCallback A {@link BookListCallback} used for notifying clients
     */
    void getBookList(BookListCallback bookListCallback, int paramInt, int flag);


    void getBookDetails(String isbn, BookDetailsCallback bookDetailsCallback);

    void getFavoriteBookList(BookListCallback bookListCallback);

    void getSearchBookList(BookListCallback bookListCallback, String searthText);
}
