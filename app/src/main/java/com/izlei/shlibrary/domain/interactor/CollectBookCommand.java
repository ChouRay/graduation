package com.izlei.shlibrary.domain.interactor;

import android.content.Context;

import com.izlei.shlibrary.data.repository.SetUpBookDataRepository;
import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.repository.SetUpBookRepository;

/**
 * Created by zhouzili on 2015/5/18.
 */
public class CollectBookCommand implements Command {
    private final SetUpBookRepository setUpBookRepository;

    public CollectBookCommand() {
        setUpBookRepository = new SetUpBookDataRepository();
    }

    @Override
    public void execute(Context context, Object object) {
        setUpBookRepository.collectBook(context, (Book)object);
    }

    @Override
    public void run() {

    }
}
