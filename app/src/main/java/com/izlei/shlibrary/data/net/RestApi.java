package com.izlei.shlibrary.data.net;

import com.izlei.shlibrary.data.entity.BookEntity;

/**
 * Created by zhouzili on 2015/4/28.
 */
public interface RestApi {
    static final String API_ISBN_BASE_URL = "https://api.douban.com/v2/book/isbn/";

    interface BookDetailsCallback {
        void onBookEntityLoaded(BookEntity bookEntity);
        void onError(Exception e);
    }

    void getBookDetailByIsbn(final String isbn, final BookDetailsCallback bookDetailsCallback);

}
