package com.izlei.shlibrary.data.repository;

import android.content.Context;
import android.util.Log;

import com.izlei.shlibrary.data.entity.BookEntity;
import com.izlei.shlibrary.data.entity.mapper.BookEntityDataMapper;
import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.repository.SetUpBookRepository;
import com.izlei.shlibrary.utils.ToastUtil;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zhouzili on 2015/5/6.
 */
public class SetUpBookDataRepository implements SetUpBookRepository{

    private BookEntityDataMapper bookEntityDataMapper;
    public SetUpBookDataRepository() {
        bookEntityDataMapper = new BookEntityDataMapper();
    }

    @Override
    public void createBook(Context context, Book book) {
        BookEntity bookEntity = this.bookEntityDataMapper.transform(book);
        saveBook(context, bookEntity);
    }

    @Override
    public void updateBook(Context context, Book book) {

    }

    private void saveBook(final Context context, final BookEntity bookEntity) {
        bookEntity.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.e(SetUpBookDataRepository.this.getClass().getSimpleName(), "createBook success");
                ToastUtil.show("Create Success!");
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
