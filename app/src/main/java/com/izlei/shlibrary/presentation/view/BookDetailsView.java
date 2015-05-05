package com.izlei.shlibrary.presentation.view;

import com.izlei.shlibrary.presentation.model.BookModel;


/**
 * Created by zhouzili on 2015/4/28.
 */
public interface BookDetailsView extends LoadDataView{

    /**
     * Render a user list in the UI.
     *
     * @param bookModel The List of {@link BookModel } that will be shown.
     */
    void renderBookDetails(BookModel bookModel);
}
