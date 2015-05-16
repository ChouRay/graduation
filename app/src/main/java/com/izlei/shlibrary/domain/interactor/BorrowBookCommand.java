package com.izlei.shlibrary.domain.interactor;

import android.content.Context;

import com.izlei.shlibrary.data.repository.SetUpBookDataRepository;
import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.repository.SetUpBookRepository;

/**
 * Created by zhouzili on 2015/5/6.
 */
public class BorrowBookCommand implements Command {
    private final SetUpBookRepository setUpBookRepository;

    public BorrowBookCommand() {
        this.setUpBookRepository = new SetUpBookDataRepository();
    }

    @Override
    public void execute(Context context, Object book) {
        setUpBookRepository.borrowBook(context, (Book)book);
    }

    @Override
    public void run() {

    }
}
