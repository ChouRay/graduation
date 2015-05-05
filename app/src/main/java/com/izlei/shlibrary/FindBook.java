package com.izlei.shlibrary;

import android.content.Context;
import android.os.Message;

import com.izlei.shlibrary.bean.Book;
import com.izlei.shlibrary.utils.ToastUtil;


import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zhouzili on 2015/3/27.
 */
public class FindBook {

    public final static int FIND_ALL_SUCCESS = 0;
    public final static int FIND_SKIP_SUCCESS = 1;
    public final static int FIND_ITEM_SUCCESS = 3;
    private ArrayList<IFindBookObserver> observers = new ArrayList<>();


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
            }

            @Override
            public void onError(int i, String s) {
            }
        });
    }



    /***
     * 查询前十个数据
     */
    public void findALl() {
        BmobQuery<Book> query = new BmobQuery<>();
        query.setLimit(10);
        query.order("-updatedAt");
        query.findObjects(context, new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> object) {
                ToastUtil.show("findAll success!"+object.size());
                notifyObservers(FindBook.FIND_ALL_SUCCESS, object);
            }
            @Override
            public void onError(int code, String msg) {
                ToastUtil.show("findAll Error!"+msg);
            }
        });
    }

    /**
     * 分页查询
     * @param newSkip
     */
    public void skipFind(int newSkip) {
        BmobQuery<Book> query = new BmobQuery<>();
        query.setLimit(10);
        query.order("-updatedAt");
        query.setSkip(newSkip);
        query.findObjects(context, new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> object) {
                ToastUtil.show("find success! "+object.size());
                notifyObservers(FindBook.FIND_SKIP_SUCCESS, object);
            }

            @Override
            public void onError(int code, String msg) {
                ToastUtil.show("findAll Error!"+msg);
            }
        });
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            for (IFindBookObserver s : observers) {
                if (s != null) {
                    s.update(msg.what, (List<Book>) msg.obj);
                }
            }
        }
    };

    private void notifyObservers(int flag, List<Book> book) {
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
        public void update(int flag, List<Book> book);
    }

}
