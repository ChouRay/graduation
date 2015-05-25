package com.izlei.shlibrary.domain.interactor;

import com.izlei.shlibrary.domain.BookBar;
import com.izlei.shlibrary.domain.exception.ErrorBundle;

import java.util.List;

/**
 * Created by zhouzili on 2015/5/24.
 */
public interface GetMomentListUseCase extends Interactor {

    interface Callback {
        void onMomentsListLoaded(List<BookBar> bookBarList);
        void onError(ErrorBundle errorBundle);
    }

    void execute(Callback callback);
}
