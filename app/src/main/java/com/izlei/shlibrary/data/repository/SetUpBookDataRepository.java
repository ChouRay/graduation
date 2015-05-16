package com.izlei.shlibrary.data.repository;

import android.content.Context;
import android.util.Log;

import com.izlei.shlibrary.data.entity.BookEntity;
import com.izlei.shlibrary.data.entity.UserEntity;
import com.izlei.shlibrary.data.entity.mapper.BookEntityDataMapper;
import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.repository.SetUpBookRepository;
import com.izlei.shlibrary.utils.ToastUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zhouzili on 2015/5/6.
 */
public class SetUpBookDataRepository implements SetUpBookRepository{

    private static final String TAG = SetUpBookDataRepository.class.getSimpleName();

    private BookEntityDataMapper bookEntityDataMapper;

    boolean isAdded = false;
    boolean isBorrowed = false;
    boolean isSendBack = false;

    public SetUpBookDataRepository() {
        bookEntityDataMapper = new BookEntityDataMapper();
    }

    @Override
    public void createBook(Context context, Book book) {
        if (!isAdded) {
            findBookByIsbn(context, book);
            isAdded = true;
        }else {
            ToastUtil.show("Has Added!");
        }
    }

    @Override
    public void borrowBook(Context context, Book book) {
        if (!isBorrowed) {
            UserEntity user = BmobUser.getCurrentUser(context,UserEntity.class);
            if (user != null) {
                this.findBookByIsbnForBorrow(context, book, user);
                isBorrowed = true;
            }else {
                ToastUtil.show("Please Login!");
            }
        }else {
            ToastUtil.show("Has Borrowed");
        }
    }

    @Override
    public void sendBackBook(Context context, Book book) {
        if (!isSendBack) {
            this.findBookByIsbn(context, book);
        }
    }

    private void saveBook(final Context context, final BookEntity bookEntity) {
        bookEntity.setStock(bookEntity.getStock() + 1);
        bookEntity.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.e(SetUpBookDataRepository.this.getClass().getSimpleName(), "createBook success");
                ToastUtil.show("Operate Success!");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e(TAG, "i==" + i + "***s==" + s);
            }
        });
    }


    private void findBookByIsbnForBorrow(final Context context,final Book book, final UserEntity user) {

        BmobQuery<BookEntity> query = new BmobQuery<>();
        query.addWhereEqualTo("isbn13", book.getIsbn13());
        query.findObjects(context, new FindListener<BookEntity>() {
            @Override
            public void onSuccess(List<BookEntity> bookEntities) {
                if (bookEntities.size() != 0 && bookEntities.get(0).getStock() >0) {
                    /*relative to user*/
                    Relations relations = new Relations(user);
                    relations.saveCurrentBorrow(bookEntities.get(0));
                    updateBookStock(context, bookEntities.get(0), -1);   //add a book
                    ToastUtil.show("Borrow book Success!");

                } else {
                    ToastUtil.show("Cant borrowing this book!");
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "findBookByIsbn Error " + s);
            }
        });
    }
    private void findBookByIsbn(final Context context,final Book book) {
        BmobQuery<BookEntity> query = new BmobQuery<>();
        query.addWhereEqualTo("isbn13", book.getIsbn13());
        query.findObjects(context, new FindListener<BookEntity>() {
            @Override
            public void onSuccess(List<BookEntity> bookEntities) {
                if (bookEntities.size() != 0) {
                    updateBookStock(context, bookEntities.get(0),+1);   //add a book
                }else {
                    //Not found this book in cloud then save book directly
                    saveBook(context, bookEntityDataMapper.transform(book));
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "findBookByIsbn Error " + s);
            }
        });
    }

    private void updateBookStock(final Context context,final BookEntity bookEntity, int num) {
        bookEntity.setStock(bookEntity.getStock() + num);
        bookEntity.update(context, bookEntity.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                ToastUtil.show("UpdateBookStock Success!");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e(TAG, s);
            }
        });
    }

}
