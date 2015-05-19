package com.izlei.shlibrary.data.repository.datasource;

import com.izlei.shlibrary.data.entity.BookEntity;

import java.util.List;

/**
 * Created by zhouzili on 2015/4/24.
 */
public interface BookDataStore {

    /**
     * callback used for clients to be notified when either a book list has been loaded or
     * any error happened.
     */
    interface BookListCallback{
        void onBookListLoaded(List<?> bookEntities);
        void onError(Exception e);
    }

    interface BookDetailsCallback {
        void onBookEntityLoaded(BookEntity bookEntity);
        void onError(Exception e);
    }


    /**
     * get a list of {@link com.izlei.shlibrary.domain.Book}
     *  @param bookListCallback A {@link com.izlei.shlibrary.data.repository.datasource.BookDataStore.BookListCallback}
     *                          used for notify clients.
     */
    public void getBooksEntityList(BookListCallback bookListCallback, int paramInt);

    public void getSkipBooksEntityList(BookListCallback bookListCallback, int paramInt);
    public void getRefreshBooksEntityList(BookListCallback bookListCallback, int paramInt);

    public void getBookEntityDetails(String isbn, BookDetailsCallback bookDetailsCallback);


    public void getFavoriteEntityList(BookListCallback bookListCallback);


}
