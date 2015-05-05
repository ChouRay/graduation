package com.izlei.shlibrary.presentation.view;

import com.izlei.shlibrary.presentation.model.BookModel;

import java.util.List;

/**
 * Interface representing a View in a model view presenter (MVP) pattern.
 * In this case is used as a view representing a list of {@link com.izlei.shlibrary.presentation.model.BookModel}.
 *
 * Created by zhouzili on 2015/4/20.
 */
public interface BookListView extends LoadDataView {

    /**
     * Render a user list in the UI.
     *
     * @param booksList The List of {@link BookModel } that will be shown.
     */
    void renderBookList(List<BookModel> booksList);

    /**
     * View a {@link BookModel} profile/details
     * @param bookModel The book will be shown.
     */
    void viewBook(BookModel bookModel);

}
