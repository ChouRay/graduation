package com.izlei.shlibrary.presentation.mapper;

import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.presentation.model.BookModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Mapper class to transform {@link Book} (in the domain layer) to {@link BookModel}
 * in the presenter layer.
 *
 * Created by zhouzili on 2015/4/20.
 */
public class BookModelDataMapper {

    @Inject
    public BookModelDataMapper() {}

    /***
     * Transform a {@link Book} into an {@link BookModel}
     * @param book book Object to be transformed.
     * @return {@link BookModel}
     */
    public BookModel transform(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("can not transform a null value");
        }
        BookModel bookModel = new BookModel();

        bookModel.setRating(book.getRating());
        bookModel.setSubtitle(book.getSubtitle());
        bookModel.setAuthor(book.getAuthor());
        bookModel.setPubdate(book.getPubdate());
        bookModel.setTags(book.getTags());
        bookModel.setOrigin_title(book.getOrigin_title());
        bookModel.setImage(book.getImage());
        bookModel.setBinding(book.getBinding());
        bookModel.setTranslator(book.getTranslator());
        bookModel.setCatalog(book.getCatalog());
        bookModel.setPages(book.getPages());
        bookModel.setImages(book.getImages());
        bookModel.setAlt(book.getAlt());
        bookModel.setId(book.getId());
        bookModel.setPublisher(book.getPublisher());
        bookModel.setIsbn10(book.getIsbn10());
        bookModel.setIsbn13(book.getIsbn13());
        bookModel.setTitle(book.getTitle());
        bookModel.setUrl(book.getUrl());
        bookModel.setAlt_title(book.getAlt_title());
        bookModel.setAuthor_intro(book.getAuthor_intro());
        bookModel.setSummary(book.getSummary());
        bookModel.setPrice(book.getPrice());

        return bookModel;
    }


    /**
     * Transform a List of {@link Book} into a List of {@link BookModel}
     * @param bookList  Objects to be transformed.
     * @return List  of {@link BookModel}
     */
    public List<BookModel> transform(List<Book> bookList) {
        List<BookModel> bookModelsList;
        if (bookList != null && !bookList.isEmpty()) {
            bookModelsList = new ArrayList<>();
            for (Book book : bookList) {
                bookModelsList.add(transform(book));
            }
        }else {
            bookModelsList = null;
        }
        return bookModelsList;
    }


    public Book transform(BookModel bookModel) {
        if (bookModel == null) {
            throw new IllegalArgumentException("can not transform a null value");
        }
        Book book = new Book();

        book.setRating(bookModel.getRating());
        book.setSubtitle(bookModel.getSubtitle());
        book.setAuthor(bookModel.getAuthor());
        book.setPubdate(bookModel.getPubdate());
        book.setTags(bookModel.getTags());
        book.setOrigin_title(bookModel.getOrigin_title());
        book.setImage(bookModel.getImage());
        book.setBinding(bookModel.getBinding());
        book.setTranslator(bookModel.getTranslator());
        book.setCatalog(bookModel.getCatalog());
        book.setPages(bookModel.getPages());
        book.setImages(bookModel.getImages());
        book.setAlt(bookModel.getAlt());
        book.setId(bookModel.getId());
        book.setPublisher(bookModel.getPublisher());
        book.setIsbn10(bookModel.getIsbn10());
        book.setIsbn13(bookModel.getIsbn13());
        book.setTitle(bookModel.getTitle());
        book.setUrl(bookModel.getUrl());
        book.setAlt_title(bookModel.getAlt_title());
        book.setAuthor_intro(bookModel.getAuthor_intro());
        book.setSummary(bookModel.getSummary());
        book.setPrice(bookModel.getPrice());

        return book;
    }
}
