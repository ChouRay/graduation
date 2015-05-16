package com.izlei.shlibrary.presentation.presenter;


import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.exception.ErrorBundle;
import com.izlei.shlibrary.domain.interactor.BorrowBookCommand;
import com.izlei.shlibrary.domain.interactor.Command;
import com.izlei.shlibrary.domain.interactor.DonateBookCommand;
import com.izlei.shlibrary.domain.interactor.GetBookDetailsUseCase;
import com.izlei.shlibrary.domain.interactor.GetBookDetailsUseCaseImpl;
import com.izlei.shlibrary.domain.interactor.SendBackBookCommand;
import com.izlei.shlibrary.presentation.mapper.BookModelDataMapper;
import com.izlei.shlibrary.presentation.model.BookModel;
import com.izlei.shlibrary.presentation.view.BookDetailsView;

/**
 * Created by zhouzili on 2015/4/28.
 */
public class BookDetailsPresenter implements Presenter {


    private BookDetailsView bookDetailsView;
    private final GetBookDetailsUseCase getBookDetailsUseCase;
    private final BookModelDataMapper bookModelDataMapper;

    private Command createBookCommand;
    private Command borrowBookCommand;
    private Command sendBackBookCommand;

    public BookDetailsPresenter() {
        getBookDetailsUseCase = new GetBookDetailsUseCaseImpl();
        bookModelDataMapper = new BookModelDataMapper();
    }

    public void setView(BookDetailsView view) {
        this.bookDetailsView = view;
    }

    private void showBookDetailsInView(Book book) {
        BookModel bookModel = this.bookModelDataMapper.transform(book);
        this.bookDetailsView.renderBookDetails(bookModel);
    }

    public void LoadBookDetails(final String isbn) {
        this.getBookDetailsUseCase.execute(isbn,bookDetailsCallback);
        Thread thread = new Thread(this.getBookDetailsUseCase);
        thread.start();
    }

    private GetBookDetailsUseCase.Callback bookDetailsCallback = new
            GetBookDetailsUseCase.Callback() {
                @Override
                public void onBookDataLoaded(Book book) {
                    BookDetailsPresenter.this.showBookDetailsInView(book);
                }

                @Override
                public void onError(ErrorBundle errorBundle) {

                }
            };

    public void createBookIntoRepository(BookModel bookModel) {
        Book book = this.bookModelDataMapper.transform(bookModel);
        if (createBookCommand == null) {
            createBookCommand = new DonateBookCommand();
        }
        createBookCommand.execute(this.bookDetailsView.getContext(), book);
    }

    public void borrowBookFromRepository(BookModel bookModel) {
        Book book = this.bookModelDataMapper.transform(bookModel);
        if (borrowBookCommand == null) {
            borrowBookCommand = new BorrowBookCommand();
        }
        borrowBookCommand.execute(this.bookDetailsView.getContext(), book);
    }

    public void sendBackBookToRepository(BookModel bookModel) {
        Book book = this.bookModelDataMapper.transform(bookModel);
        if (sendBackBookCommand == null) {
            sendBackBookCommand = new SendBackBookCommand();
        }
        sendBackBookCommand.execute(this.bookDetailsView.getContext(),book);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }
}
