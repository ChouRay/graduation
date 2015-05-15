package com.izlei.shlibrary.demo;

import android.content.Context;
import android.os.Looper;
import android.os.Message;

import com.izlei.shlibrary.presentation.view.activity.PersonalActivity;
import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.CurrentBorrow;
import com.izlei.shlibrary.domain.User;
import com.izlei.shlibrary.utils.ToastUtil;


import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.util.Log;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zhouzili on 2015/3/27.
 */
public class FindBook {

    public final String TAG = "in FindBook.class";
    public final static int FIND_ALL_SUCCESS = 0;
    public final static int FIND_SKIP_SUCCESS = 1;
    public final static int FIND_ITEM_SUCCESS = 3;
    public final static int FIND_ITEM_FAILURE = 4;
    public final static int FIND_NEW_SUCCESS = 5;
    private  ArrayList<IFindBookObserver> observers = new ArrayList<>();


    private Context context;
    public FindBook(Context context){
        this.context = context;
    }

    public void findBookByIsbn(String isbn) {
        BmobQuery<Book> query = new BmobQuery<>();
        query.setLimit(1000);
        query.addWhereEqualTo("isbn", isbn);
        query.findObjects(context, new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> books) {
               notifyObservers(FIND_ITEM_SUCCESS, books);
               Log.e(TAG, "findBookByIsbn: "+books.size());
            }

            @Override
            public void onError(int i, String s) {
                notifyObservers(FIND_ITEM_FAILURE, null);
                Log.e(TAG, "findBookByIsbn Error "+s);
            }
        });
    }

    /***
     * 查询前十个数据
     */
    public void findALl() {
        BmobQuery<Book> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);// 先从缓存取数据，无论结果如何都会再次从网络获取数据
        query.setLimit(10);
        query.order("-updatedAt");
        query.findObjects(context, new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> object) {
                Log.e(TAG, "Find Success "+object.size());
                notifyObservers(FindBook.FIND_ALL_SUCCESS, object);
                final int size = object.size();

                /*不这样处理则：ToastUtil报错： Uncaught handler: thread Thread-8 exiting due to uncaught exception
                * 原因是非主线程中默认没有创建Looper对象，需要先调用Looper.prepare()启用Looper*/
                new Thread() {
                    public void run() {
                        Looper.prepare();
                        ToastUtil.show("发现"+size+"本书");
                        Looper.loop();
                    }
                }.start();
            }
            @Override
            public void onError(int code, String msg) {
                Log.e(TAG, "Find Error"+ msg);
            }
        });
    }

    public void findNew() {
        BmobQuery<Book> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);// 只从网络获取数据，同时会在本地缓存数据
        query.setLimit(10);
        query.order("-updatedAt");
        query.findObjects(context, new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> object) {
                Log.e(TAG, "Find Success "+object.size());
                notifyObservers(FindBook.FIND_NEW_SUCCESS, object);
                final int size = object.size();

                /*不这样处理则：ToastUtil报错： Uncaught handler: thread Thread-8 exiting due to uncaught exception
                * 原因是非主线程中默认没有创建Looper对象，需要先调用Looper.prepare()启用Looper*/
                new Thread() {
                    public void run() {
                        Looper.prepare();
                        ToastUtil.show("发现"+size+"本书");
                        Looper.loop();
                    }
                }.start();
            }
            @Override
            public void onError(int code, String msg) {
                Log.e(TAG, "Find Error"+ msg);
            }
        });
    }

    /**
     * 分页查询
     * @param newSkip
     */
    public void skipFind(int newSkip) {
        BmobQuery<Book> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.setLimit(10);
        query.order("-updatedAt");
        query.setSkip(newSkip);
        query.findObjects(context, new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> object) {
                notifyObservers(FindBook.FIND_SKIP_SUCCESS, object);
                if (object.size() == 0){
                    ToastUtil.show("没有啦！");
                }
            }

            @Override
            public void onError(int code, String msg) {
                Log.e(TAG, "Skip Find Error "+msg);
            }
        });
    }

    /**
     * 查找当前用户借阅的书籍
     * @param user
     */
    public void findCurrentBorrows(User user) {
        BmobQuery<CurrentBorrow> books = new BmobQuery<>();
        books.addWhereRelatedTo("currentBorrow", new BmobPointer(user));
        books.findObjects(context, new FindListener<CurrentBorrow>() {
            @Override
            public void onSuccess(List<CurrentBorrow> currentBorrows) {
                notifyObservers(PersonalActivity.CURRENT_BORROWED, currentBorrows);
                if (currentBorrows.size() == 0) {
                    ToastUtil.show("赶快借本书看看吧！");
                }
            }
            @Override
            public void onError(int i, String s) {

            }
        });
    }


    private  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            for (IFindBookObserver s : observers) {
                if (s != null) {
                    s.update(msg.what, (List<?>) msg.obj);
                }
            }
        }
    };

    private void notifyObservers(int flag, List<?> book) {
        mHandler.sendMessage(mHandler.obtainMessage(flag, book));
    }
    public void addObserver(IFindBookObserver s) {
        synchronized (context) {
            if (!observers.contains(s)) {
                observers.add(s);
            }
        }
    }
    public interface IFindBookObserver {
        public void update(int flag, List<?> books);
    }

}
