package com.izlei.shlibrary;

import android.os.Message;

import com.com.izlei.app.AppController;
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
    public final  static int FIND_SUCESS = 0;
    private ArrayList<IFindBookObserver> observers = new ArrayList<>();
    List<Book> bookList;
    public BmobQuery<Book> findBookByIsbn(String isbn) {
        BmobQuery<Book> query = new BmobQuery<>();

        return query.addWhereEqualTo("isbn", isbn);
    }

    public void findALl() {
        BmobQuery<Book> query = new BmobQuery<>();
        query.findObjects(AppController.getInstance(), new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> object) {
                ToastUtil.show("findAll success!"+object.size());
                notifyObservers(FindBook.FIND_SUCESS, object);
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
        synchronized (AppController.getInstance()) {
            if (!observers.contains(s)) {
                observers.add(s);
            }
        }
    }
    public interface IFindBookObserver {
        public void update(int flag, List<Book> book);
    }

}
