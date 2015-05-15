package com.izlei.shlibrary.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.izlei.shlibrary.data.entity.BookEntity;
import com.izlei.shlibrary.data.entity.mapper.BookEntityDataMapper;
import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.repository.SetUpBookRepository;
import com.izlei.shlibrary.utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.bmob.v3.BmobQuery;
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
    public void updateBook(Context context, Book book) {
    }

    private void saveBook(final Context context, final BookEntity bookEntity) {
        bookEntity.setStock(bookEntity.getStock() + 1);
        bookEntity.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.e(SetUpBookDataRepository.this.getClass().getSimpleName(), "createBook success");
                ToastUtil.show("Create Success!");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e(TAG, "i==" + i + "***s==" + s);
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
                    updateBookStock(context, bookEntities.get(0));
                }else { //Not found this book in cloud then save book directly
                    saveBook(context, bookEntityDataMapper.transform(book));
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "findBookByIsbn Error " + s);
            }
        });
    }

    private void updateBookStock(final Context context,final BookEntity bookEntity) {
        bookEntity.setStock(bookEntity.getStock() + 1);
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
