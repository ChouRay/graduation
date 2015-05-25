package com.izlei.shlibrary.domain.interactor;

import android.content.Context;

import com.izlei.shlibrary.data.repository.BookBarDataRepository;
import com.izlei.shlibrary.domain.BookBar;
import com.izlei.shlibrary.domain.repository.BookBarRepository;

/**
 * Created by zhouzili on 2015/5/24.
 */
public class CommentCommand implements Command {

    private BookBarRepository bookBarRepository;

    public CommentCommand() {
        this.bookBarRepository = new BookBarDataRepository();
    }

    @Override
    public void execute(Context context, Object object) {
        this.bookBarRepository.createComment(context, (BookBar)object);
    }

    @Override
    public void run() {

    }
}
