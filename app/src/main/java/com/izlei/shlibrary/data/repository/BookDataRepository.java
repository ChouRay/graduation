package com.izlei.shlibrary.data.repository;


import com.izlei.shlibrary.app.AppController;
import com.izlei.shlibrary.data.entity.BookEntity;
import com.izlei.shlibrary.data.entity.FavoriteEntity;
import com.izlei.shlibrary.data.entity.mapper.BookEntityDataMapper;
import com.izlei.shlibrary.data.entity.mapper.BookEntityJsonMapper;
import com.izlei.shlibrary.data.excption.BookNotFoundException;
import com.izlei.shlibrary.data.excption.RepositoryErrorBundle;
import com.izlei.shlibrary.data.net.RestApi;
import com.izlei.shlibrary.data.net.RestApiImpl;
import com.izlei.shlibrary.data.repository.datasource.BookDataStore;
import com.izlei.shlibrary.data.repository.datasource.CloudBookDataStore;
import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.repository.BookRepository;

import java.util.List;


/**
 * Created by zhouzili on 2015/4/23.
 */
public class BookDataRepository implements BookRepository{
    public static final String TAG = BookDataRepository.class.getSimpleName();
    public static final int DEFAULT_FLAG = 1;
    public static final int LOAD_MORE_FLAG = 2;
    public static final int REFRESH_FLAG = 3;
    private BookListCallback bookListCallback;

    private BookEntityDataMapper bookEntityDataMapper;
    public BookDataRepository() {
        bookEntityDataMapper = new BookEntityDataMapper();
    }

    @Override
    public void getBookList(final BookListCallback bookListCallback, final int paramInt, int flag) {
        final BookDataStore bookDataStore = new CloudBookDataStore();       //*need to improve*/
        this.bookListCallback = bookListCallback;
        if (flag == DEFAULT_FLAG) {
            bookDataStore.getBooksEntityList(listCallback,paramInt);
        }
       if (flag == REFRESH_FLAG) {
            bookDataStore.getRefreshBooksEntityList(listCallback,paramInt);
        }

        if (flag == LOAD_MORE_FLAG) {
            bookDataStore.getSkipBooksEntityList(listCallback,paramInt);
        }
    }

    @Override
    public void getBookDetails(final String isbn, final BookDetailsCallback bookDetailsCallback) {
        BookEntityJsonMapper bookEntityJsonMapper = new BookEntityJsonMapper();
        RestApi restApi = new RestApiImpl(AppController.getInstance(), bookEntityJsonMapper);
        final BookDataStore bookDataStore = new CloudBookDataStore(restApi);

        bookDataStore.getBookEntityDetails(isbn,new BookDataStore.BookDetailsCallback() {
            @Override
            public void onBookEntityLoaded(BookEntity bookEntity) {
                Book book = BookDataRepository.this.bookEntityDataMapper.transform(bookEntity);
                if (book != null ) {
                    bookDetailsCallback.onBookDetailsLoaded(book);
                }else {
                    bookDetailsCallback.onError(new RepositoryErrorBundle(new BookNotFoundException()));
                }
            }

            @Override
            public void onError(Exception e) {
                bookDetailsCallback.onError(new RepositoryErrorBundle(e));
            }
        });
    }



    private BookDataStore.BookListCallback listCallback = new BookDataStore.BookListCallback() {
        @Override
        public void onBookListLoaded(List<?> bookEntities) {
            List<Book> bookList =getTransformBook((List<BookEntity>)bookEntities);
            bookListCallback.onBookListLoaded(bookList);
        }

        @Override
        public void onError(Exception e) {
            bookListCallback.onError(new RepositoryErrorBundle(e));
        }
    };

    @Override
    public void getFavoriteBookList(final BookListCallback bookListCallback) {
        this.bookListCallback = bookListCallback;
        final BookDataStore bookDataStore = new CloudBookDataStore();
        bookDataStore.getFavoriteEntityList(new BookDataStore.BookListCallback() {
            @Override
            public void onBookListLoaded(List<?> bookEntities) {
                bookListCallback.onBookListLoaded(
                        getTransformBookFavarite(
                                (List<FavoriteEntity>)bookEntities));
            }

            @Override
            public void onError(Exception e) {
                bookListCallback.onError(new RepositoryErrorBundle(new BookNotFoundException()));
            }
        });
    }

    private List<Book> getTransformBookFavarite(List<FavoriteEntity> favoriteEntities) {
        return this.bookEntityDataMapper.transformFavorite(favoriteEntities);
    }
    private List<Book> getTransformBook(List<BookEntity> bookEntities){
        return BookDataRepository.this.bookEntityDataMapper.transform(bookEntities);
    }
}
