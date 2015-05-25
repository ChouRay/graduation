package com.izlei.shlibrary.presentation.view;

import com.izlei.shlibrary.presentation.model.BookBarModel;

import java.util.List;

/**
 * Created by zhouzili on 2015/5/24.
 */
public interface LoadBookBarView extends LoadDataView {
    void rendererMoments(List<BookBarModel> bookBarModelList);
    void rendererMoment(BookBarModel bookBarModel);
}
