package com.izlei.shlibrary.presentation.mapper;

import com.izlei.shlibrary.domain.BookBar;
import com.izlei.shlibrary.presentation.model.BookBarModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouzili on 2015/5/24.
 */
public class BookBarModelDataMapper {

    public List<BookBarModel> transform(List<BookBar> list) {
        List<BookBarModel> bookBarModels = new ArrayList<>();
        BookBarModel bookBarModel = null;

        for(BookBar bookBar : list) {
            bookBarModel = this.transform(bookBar);
            bookBarModels.add(bookBarModel);
        }
        return bookBarModels;
    }

    public BookBarModel transform(BookBar bookBar) {
        BookBarModel bookBarModel = null;
        if (bookBar != null) {
            bookBarModel = new BookBarModel();
            bookBarModel.setUsername(bookBar.getUsername());
            bookBarModel.setLeaveWords(bookBar.getLeaveWords());
            bookBarModel.setUpdatedAt(bookBar.getUpdatedAt());
        }
        return bookBarModel;
    }

    public BookBar transform(BookBarModel bookBarModel) {
        BookBar bookBar = null;
        if (bookBarModel != null) {
            bookBar = new BookBar();
            bookBar.setUsername(bookBarModel.getUsername());
            bookBar.setLeaveWords(bookBarModel.getLeaveWords());
            bookBar.setUpdatedAt(bookBarModel.getUpdatedAt());
        }
        return bookBar;
    }
}
