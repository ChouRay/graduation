package com.izlei.shlibrary.data.repository;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;

import com.izlei.shlibrary.app.AppController;
import com.izlei.shlibrary.data.entity.BookEntity;
import com.izlei.shlibrary.data.entity.CurrentBorrow;
import com.izlei.shlibrary.data.entity.FavoriteEntity;
import com.izlei.shlibrary.data.entity.UserEntity;
import com.izlei.shlibrary.data.excption.BookNotFoundException;
import com.izlei.shlibrary.data.repository.datasource.BookDataStore;
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
    FavoriteEntity favoriteEntity;

    public Relations() {
        this.user = BmobUser.getCurrentUser(AppController.getInstance(), UserEntity.class);
    }
    public Relations(UserEntity user) {
        this.user = user;
    }
    public void saveCurrentBorrow(final BookEntity book) {
        if (user == null) {
            Log.e(getClass().getSimpleName(),"Current User is Null!");
            return;
        }
        if (currentBorrow == null) {
            currentBorrow = new CurrentBorrow();
        }
        currentBorrow.setTitle(book.getTitle());
        currentBorrow.setIsbn(book.getIsbn13());
        currentBorrow.setBorrowDate(System.currentTimeMillis());
        currentBorrow.setSendbackDate(System.currentTimeMillis(), 30);  //return the book date for 30 days
        currentBorrow.setUserEntity(user);
        currentBorrow.save(AppController.getInstance(), new SaveListener() {
            @Override
            public void onSuccess() {
                Log.e(Relations.class.getSimpleName(), "save current_borrow success!");
                SetUpBookDataRepository setUpBookDataRepository = new SetUpBookDataRepository();
                setUpBookDataRepository.updateBookBorrowedCount(book);
                addCurrentBorrowToUser();
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e(Relations.class.getSimpleName(), "save current_borrow failure! " + s);
            }
        });
    }

    private void addCurrentBorrowToUser() {
        if (user == null || currentBorrow == null) {
            Log.e(getClass().getCanonicalName(), "current user of current borrow is null");
            return;
        }
        BmobRelation relation = new BmobRelation();
        relation.add(currentBorrow);
        user.setCurrentBorrowRelation(relation);
        user.update(AppController.getInstance(), new UpdateListener() {
            @Override
            public void onSuccess() {
                ToastUtil.show("addCurrentBorrowToUser success!");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.show("Sorry! addCurrentBorrowToUser failure！ " + s);
            }
        });
    }

    public void findMyCurrentBorrow() {
        BmobQuery<CurrentBorrow> books = new BmobQuery<>();
        books.addWhereRelatedTo("currentBorrowRelation", new BmobPointer(user));
        books.findObjects(AppController.getInstance(), new FindListener<CurrentBorrow>() {
            @Override
            public void onSuccess(List<CurrentBorrow> currentBorrows) {
                relationBook.getCurrentBorrowedBook(currentBorrows);
                for (CurrentBorrow book : currentBorrows) {
                    Log.d("**findMyCurrentBorrow**", book.getTitle());
                }
            }

            @Override
            public void onError(int i, String s) {
            }
        });
    }

    private void addFavoriteToUser (Context context) {
        if (user == null ||favoriteEntity ==null) {
            Log.e(getClass().getCanonicalName(), "current user or favoriteEntity of favorite is null");
            return;
        }
        BmobRelation relation = new BmobRelation();
        relation.add(favoriteEntity);
        user.setFavoriteRelation(relation);
        user.update(context, new UpdateListener() {
            @Override
            public void onSuccess() {
                ToastUtil.show("add favorite to user success!");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.show("Sorry! add favorite to user failure！ " + s);
            }
        });
    }

    public void saveFavariteBook(final Context context, final BookEntity bookEntity) {
        if (user == null) {
            Log.e(getClass().getSimpleName(), "Current user is null");
            return;
        }

        if (favoriteEntity == null) {
            favoriteEntity = new FavoriteEntity();
        }
        favoriteEntity.setUserEntity(user);
        favoriteEntity.setImage(bookEntity.getImage());
        favoriteEntity.setTitle(bookEntity.getTitle());
        favoriteEntity.setPrice(bookEntity.getPrice());
        favoriteEntity.setIsbn13(bookEntity.getIsbn13());
        favoriteEntity.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                addFavoriteToUser(context);
                Log.i(Relations.class.getSimpleName(), "save favorite success!");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(Relations.class.getSimpleName(), "save favorite error!");
            }
        });
    }


    public void findMyFavorite(final BookDataStore.BookListCallback bookListCallback) {
        if (user == null) {
            Log.e(getClass().getSimpleName(), "Current user is null");
            return;
        }
        BmobQuery<FavoriteEntity> books = new BmobQuery<>();
        books.addWhereRelatedTo("favoriteRelation", new BmobPointer(user));
        books.findObjects(AppController.getInstance(), new FindListener<FavoriteEntity>() {
            @Override
            public void onSuccess(List<FavoriteEntity> favoriteEntities) {
                bookListCallback.onBookListLoaded(favoriteEntities);
                for (FavoriteEntity book : favoriteEntities) {
                    Log.d("**findd MyFavorite**", book.getTitle());
                }
            }

            @Override
            public void onError(int i, String s) {
                bookListCallback.onError(new BookNotFoundException());
            }
        });
    }


    private IRelationBook relationBook;
    public void initIRelationBookObject(IRelationBook relationBook) {
        this.relationBook =relationBook;
    }

    public interface IRelationBook {
        void getCurrentBorrowedBook(List<?> books);
    }
}
