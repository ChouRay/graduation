package com.izlei.shlibrary.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.izlei.shlibrary.data.entity.BookEntity;
import com.izlei.shlibrary.data.entity.mapper.BookEntityJsonMapper;
import com.izlei.shlibrary.data.excption.NetworkConnectionException;

/**
 * Created by zhouzili on 2015/4/28.
 */
public class RestApiImpl implements RestApi {

    private final Context context;
    private final BookEntityJsonMapper bookEntityJsonMapper;
    public RestApiImpl(Context context, BookEntityJsonMapper bookEntityJsonMapper) {
        if (context == null || bookEntityJsonMapper == null) {
            throw new IllegalArgumentException("The constructor parameters cannot be null!");
        }
        this.context = context.getApplicationContext();
        this.bookEntityJsonMapper = bookEntityJsonMapper;
    }

    @Override
    public void getBookDetailByIsbn(String isbn, BookDetailsCallback bookDetailsCallback) {
        if (bookDetailsCallback == null) {
            throw new IllegalArgumentException("bookDetailsCallback cannot be null");
        }
        if (isThereInternetConnection()) {
            try {
                String apiUrl = RestApi.API_ISBN_BASE_URL + isbn;
                ApiConnection getBookDetailsConnection = ApiConnection.createGET(apiUrl);
                String responseBookDetails = getBookDetailsConnection.requestSyncCall();
                BookEntity bookEntity = this.bookEntityJsonMapper.transformBookEntity(responseBookDetails);

                bookDetailsCallback.onBookEntityLoaded(bookEntity);

            } catch (Exception e) {
                bookDetailsCallback.onError(e);
            }
        }else {
            bookDetailsCallback.onError(new NetworkConnectionException());
        }

    }

    private boolean isThereInternetConnection() {
        boolean isConnected;
        ConnectivityManager connectivityManager = (ConnectivityManager)this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected  = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;

    }
}
