package com.izlei.shlibrary.presentation.presenter;

import com.izlei.shlibrary.domain.BookBar;
import com.izlei.shlibrary.domain.exception.ErrorBundle;
import com.izlei.shlibrary.domain.interactor.Command;
import com.izlei.shlibrary.domain.interactor.CommentCommand;
import com.izlei.shlibrary.domain.interactor.GetMomentListUseCase;
import com.izlei.shlibrary.domain.interactor.GetMomentListUseCaseImpl;
import com.izlei.shlibrary.presentation.mapper.BookBarModelDataMapper;
import com.izlei.shlibrary.presentation.model.BookBarModel;
import com.izlei.shlibrary.presentation.view.LoadBookBarView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by zhouzili on 2015/5/24.
 */
public class BookBarPresenter implements Presenter {

    private LoadBookBarView loadBookBarView;
    private BookBarModelDataMapper bookBarModelDataMapper;

    private GetMomentListUseCase getMomentListUseCase;
    private Command commentCommand;
    BookBarModel bookBarModel;

    public BookBarPresenter() {
        getMomentListUseCase = new GetMomentListUseCaseImpl();
        bookBarModelDataMapper = new BookBarModelDataMapper();
    }

    public void setView(LoadBookBarView bookBarView) {
        this.loadBookBarView = bookBarView;
    }

    public void loadCommentList() {
        this.showViewLoading();
        this.hideViewRetry();
        this.getComments();
    }

    private void getComments() {
        getMomentListUseCase.execute(callback);
    }

    private GetMomentListUseCase.Callback callback =
            new GetMomentListUseCase.Callback() {
                @Override
                public void onMomentsListLoaded(List<BookBar> bookBarList) {
                    BookBarPresenter.this.hideViewLoading();
                    BookBarPresenter.this.showMoments(bookBarList);
                }

                @Override
                public void onError(ErrorBundle errorBundle) {
                    BookBarPresenter.this.hideViewLoading();
                    BookBarPresenter.this.showViewRetry();
                }
            };

    private void showMoments(List<BookBar> moments) {
        loadBookBarView.rendererMoments(bookBarModelDataMapper.transform(moments));
    }

    public void createComment(String text,String username) {
        if (commentCommand == null) {
            commentCommand = new CommentCommand();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd. HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());

        BookBarModel bookBarModel = new BookBarModel();
        if (username != null) {
            bookBarModel.setUsername(username);
        }else {
            bookBarModel.setUsername("匿名");
        }
        bookBarModel.setLeaveWords(text);
        bookBarModel.setUpdatedAt(format.format(curDate));

        if (loadBookBarView.getContext() != null) {
            commentCommand.execute(loadBookBarView.getContext(),
                    bookBarModelDataMapper.transform(bookBarModel));

            loadBookBarView.rendererMoment(bookBarModel);
        }
    }

    private void hideViewRetry() {
        this.loadBookBarView.hideRetry();
    }

    private void showViewRetry() {
        this.loadBookBarView.showRetry();
    }

    private void showViewLoading() {
        this.loadBookBarView.showLoading();
    }
    private void hideViewLoading() {
        this.loadBookBarView.hideLoading();
    }
    
    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }
}
