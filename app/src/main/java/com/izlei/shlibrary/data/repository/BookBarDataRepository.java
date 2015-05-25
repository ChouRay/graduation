package com.izlei.shlibrary.data.repository;

import android.content.Context;
import android.util.Log;

import com.izlei.shlibrary.app.AppController;
import com.izlei.shlibrary.data.entity.BookBarEntity;
import com.izlei.shlibrary.data.entity.mapper.BookBarEntityDataMapper;
import com.izlei.shlibrary.data.excption.RepositoryErrorBundle;
import com.izlei.shlibrary.domain.BookBar;
import com.izlei.shlibrary.domain.repository.BookBarRepository;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zhouzili on 2015/5/24.
 */
public class BookBarDataRepository implements BookBarRepository {
    private BookBarEntityDataMapper dataMapper;

    public BookBarDataRepository() {
        dataMapper = new BookBarEntityDataMapper();
    }

    @Override
    public void getMomentList(final MomentListCallback momentListCallback) {
        final BmobQuery<BookBarEntity> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.IGNORE_CACHE);
        query.setLimit(10);
        query.order("-updatedAt");
        query.findObjects(AppController.getInstance(), new FindListener<BookBarEntity>() {
            @Override
            public void onSuccess(List<BookBarEntity> list) {
                momentListCallback.onMomentsListLoaded(dataMapper.transform(list));
            }

            @Override
            public void onError(int i, String s) {
                momentListCallback.onError(new RepositoryErrorBundle(new Exception()));
            }
        });
    }

    @Override
    public void createComment(Context context, BookBar bookBar) {
        BookBarEntity bookBarEntity = dataMapper.transform(bookBar);
        bookBarEntity.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.e(BookBarDataRepository.this.getClass().getSimpleName(), "Comment Success");
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
