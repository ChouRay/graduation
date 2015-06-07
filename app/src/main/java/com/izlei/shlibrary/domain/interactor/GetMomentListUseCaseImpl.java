package com.izlei.shlibrary.domain.interactor;

import com.izlei.shlibrary.data.executor.JobExecutor;
import com.izlei.shlibrary.data.repository.BookBarDataRepository;
import com.izlei.shlibrary.domain.BookBar;
import com.izlei.shlibrary.domain.exception.ErrorBundle;
import com.izlei.shlibrary.domain.executor.PostExecutionThread;
import com.izlei.shlibrary.domain.executor.ThreadExecutor;
import com.izlei.shlibrary.domain.repository.BookBarRepository;
import com.izlei.shlibrary.presentation.UIThread;

import java.util.List;

/**
 * Created by zhouzili on 2015/5/24.
 */
public class GetMomentListUseCaseImpl implements GetMomentListUseCase {
    private Callback callback;
    private final PostExecutionThread postExecutionThread;
    private final BookBarRepository bookBarRepository;
    private final ThreadExecutor threadExecutor;
    private int skip; //skip page num

    public GetMomentListUseCaseImpl() {
        this.postExecutionThread =  new UIThread();
        threadExecutor = JobExecutor.getInstance();
        bookBarRepository = new BookBarDataRepository();
    }

    @Override
    public void execute(Callback callback, int skip) {
        this.callback = callback;
        this.skip =skip;
       threadExecutor.execute(this);
    }

    @Override
    public void run() {
        this.bookBarRepository.getMomentList(momentListCallback, skip);
    }

    private final BookBarRepository.MomentListCallback momentListCallback =
            new BookBarRepository.MomentListCallback() {
                @Override
                public void onMomentsListLoaded(List<BookBar> bookBarList) {
                    notifyMomentsLoadedSuccessfully(bookBarList);
                }

                @Override
                public void onError(ErrorBundle errorBundle) {
                    notifyError(errorBundle);
                }
            };


    private void notifyMomentsLoadedSuccessfully(final List<BookBar> bookBarList)  {
        this.postExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onMomentsListLoaded(bookBarList);
            }
        });
    }

    private void notifyError(final ErrorBundle errorBundle) {
        this.postExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(errorBundle);
            }
        });
    }

}
