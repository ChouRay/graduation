package com.izlei.shlibrary.demo;

import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;

import com.izlei.shlibrary.app.AppController;
import com.izlei.shlibrary.data.entity.UserEntity;
import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.CurrentBorrow;
import com.izlei.shlibrary.domain.User;
import com.izlei.shlibrary.utils.ToastUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zhouzili on 2015/4/3.
 */
public class Relations {

    UserEntity user;
    CurrentBorrow currentBorrow;

    public Relations() {
        user = BmobUser.getCurrentUser(AppController.getInstance(),UserEntity.class);
        currentBorrow = new CurrentBorrow();
    }
    public void saveCurrentBorrow(Book book) {
        if (TextUtils.isEmpty(user.getObjectId())) {
            ToastUtil.show("Current User is Null!");
            return;
        }
        if (currentBorrow == null) {
            currentBorrow = new CurrentBorrow();
        }
        currentBorrow.setTitle(book.getTitle());
        currentBorrow.setIsbn(book.getIsbn13());
        currentBorrow.setBorrowDate(System.currentTimeMillis());
        currentBorrow.setSendbackDate(System.currentTimeMillis(), 30);  //return the book date for 30 days
        currentBorrow.setUser(user);
        currentBorrow.save(AppController.getInstance(),new SaveListener() {
            @Override
            public void onSuccess() {
                ToastUtil.show("save current_borrow success!");
                addCurrentBorrowToUser();
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.show("save current_borrow failure!"+s);
            }
        });
    }

    private void addCurrentBorrowToUser() {
        if (TextUtils.isEmpty(user.getObjectId()) || TextUtils.isEmpty(currentBorrow.getObjectId())) {
            ToastUtil.show("current user of current borrow is null");
            return;
        }
        BmobRelation relation = new BmobRelation();
        relation.add(currentBorrow);
        user.setCurrentBorrow(relation);
        user.update(AppController.getInstance(), new UpdateListener() {
            @Override
            public void onSuccess() {
                ToastUtil.show("addCurrentBorrowToUser success!");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.show("Sorry! addCurrentBorrowToUser failure！ "+ s);
            }
        });
    }

    public void findMyCurrentBorrow() {
        BmobQuery<CurrentBorrow> books = new BmobQuery<>();

        books.addWhereRelatedTo("currentBorrow", new BmobPointer(user));
        books.findObjects(AppController.getInstance(), new FindListener<CurrentBorrow>() {
            @Override
            public void onSuccess(List<CurrentBorrow> currentBorrows) {
                ToastUtil.show("currentBorrow number == "+currentBorrows.size());
                for (CurrentBorrow book : currentBorrows) {
                    Log.d("**findMyCurrentBorrow**", book.getTitle());
                }
            }

            @Override
            public void onError(int i, String s) {
                ToastUtil.show("findMyCurrentBorrow failure!"+ s);
            }
        });
    }

    private String timeFormat() {
        Time t = new Time();
        t.setToNow();

        return t.year+"-"+t.month+"-"+t.monthDay;

    }
}
