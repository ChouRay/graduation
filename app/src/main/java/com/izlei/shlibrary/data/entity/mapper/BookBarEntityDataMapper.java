package com.izlei.shlibrary.data.entity.mapper;

import com.izlei.shlibrary.data.entity.BookBarEntity;
import com.izlei.shlibrary.domain.BookBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouzili on 2015/5/24.
 */
public class BookBarEntityDataMapper {


    public List<BookBar> transform(List<BookBarEntity> list) {
        List<BookBar> bookBars = new ArrayList<>();
        BookBar bookBar = null;

        for(BookBarEntity entity : list) {
            bookBar = this.transform(entity);
            bookBars.add(bookBar);
        }
        return bookBars;
    }

    public BookBar transform(BookBarEntity bookBarEntity) {
        BookBar bookBar = null;
        if (bookBarEntity != null) {
            bookBar = new BookBar();
            bookBar.setUsername(bookBarEntity.getUsername());
            bookBar.setLeaveWords(bookBarEntity.getLeaveWords());
            bookBar.setUpdatedAt(bookBarEntity.getUpdatedAt());
        }
        return bookBar;
    }

    public BookBarEntity transform(BookBar bookBar) {
        BookBarEntity BookBarEntity = null;
        if (bookBar != null) {
            BookBarEntity = new BookBarEntity();
            BookBarEntity.setUsername(bookBar.getUsername());
            BookBarEntity.setLeaveWords(bookBar.getLeaveWords());
        }
        return BookBarEntity;
    }
}
