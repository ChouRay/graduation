package com.izlei.shlibrary.domain.repository;

import android.content.Context;

import com.izlei.shlibrary.domain.BookBar;
import com.izlei.shlibrary.domain.exception.ErrorBundle;

import java.util.List;

/**
 * Created by zhouzili on 2015/5/24.
 */
public interface BookBarRepository {

    interface MomentListCallback {
        void onMomentsListLoaded(List<BookBar> bookBarList);
        void onError(ErrorBundle errorBundle);
    }

    void getMomentList(MomentListCallback momentListCallback, int skip);

    void createComment(Context context,BookBar bookBar);
}
