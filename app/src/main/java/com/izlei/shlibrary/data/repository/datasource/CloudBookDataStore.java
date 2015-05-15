package com.izlei.shlibrary.data.repository.datasource;



import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.izlei.shlibrary.app.AppController;
import com.izlei.shlibrary.data.entity.BookEntity;
import com.izlei.shlibrary.data.net.RestApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zhouzili on 2015/4/24.
 */
public class CloudBookDataStore implements BookDataStore {

    private static SharedPreferences sharedPreferences  = AppController.getInstance().getSharedPreferences("updatedAt", Context.MODE_PRIVATE);
    private static SharedPreferences.Editor editor = sharedPreferences.edit();
    private RestApi restApi;
    private int lock = 0;

    public CloudBookDataStore(){
    }

    public CloudBookDataStore(RestApi restApi) {
        this.restApi = restApi;
    }

    @Override
    public void getBooksEntityList(final BookListCallback bookListCallback, int paramInt) {
        final BmobQuery<BookEntity> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.IGNORE_CACHE);
        query.setLimit(10);
        query.order("-updatedAt");
        query.findObjects(AppController.getInstance(), new FindListener<BookEntity>() {
            @Override
            public void onSuccess(List<BookEntity> bookList) {
                bookListCallback.onBookListLoaded(bookList);
                editor.putString("RECENT_UPDATED", bookList.get(0).getUpdatedAt()).commit();
            }

            @Override
            public void onError(int i, String s) {
                Log.e("getBookserror",s);
            }
        });
    }

    @Override
    public void getSkipBooksEntityList(final BookListCallback bookListCallback, int paramInt) {
        BmobQuery<BookEntity> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.setLimit(10);
        query.order("-updatedAt");
        query.setSkip(paramInt);
        query.setMaxCacheAge(100000L);
        query.findObjects(AppController.getInstance(), new FindListener<BookEntity>() {
            @Override
            public void onSuccess(List<BookEntity> bookList) {
                bookListCallback.onBookListLoaded(bookList);

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    public void getRefreshBooksEntityList(final BookListCallback bookListCallback, int paramInt) {

        BmobQuery<BookEntity> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);// 只从网络获取数据，同时会在本地缓存数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            String recent_updated = sharedPreferences.getString("RECENT_UPDATED", null);
            if (recent_updated != null){
                date = sdf.parse(recent_updated);
            }
        }catch (ParseException pe) {
            pe.printStackTrace();
        }
        if (date != null) {
            query.addWhereGreaterThan("updatedAt", new BmobDate(date));
        }
        query.setLimit(100);
        query.order("-updatedAt");
        query.findObjects(AppController.getInstance(), new FindListener<BookEntity>() {
            @Override
            public void onSuccess(List<BookEntity> bookList) {
                if (bookList.size() > 0) {
                    editor.putString("RECENT_UPDATED", bookList.get(0).getUpdatedAt()).commit();
                    bookListCallback.onBookListLoaded(bookList);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    /**
     *
     * @param isbn the book isbn to retrieve book data.
     * @param bookDetailsCallback A {@link BookDetailsCallback} used for notify clients.
     */
    @Override
    public void getBookEntityDetails(final String isbn, final BookDetailsCallback bookDetailsCallback) {

        this.restApi.getBookDetailByIsbn(isbn, new RestApi.BookDetailsCallback() {
            @Override
            public void onBookEntityLoaded(BookEntity bookEntity) {
                bookDetailsCallback.onBookEntityLoaded(bookEntity);
            }

            @Override
            public void onError(Exception e) {
                bookDetailsCallback.onError(e);
            }
        });
    }
}
