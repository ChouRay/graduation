package com.izlei.shlibrary.data.entity.mapper;

import com.izlei.shlibrary.data.entity.BookEntity;
import com.izlei.shlibrary.domain.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouzili on 2015/4/23.
 */
public class BookEntityDataMapper {

    public Book transform(BookEntity bookEntity) {
        Book book = null;
        if (bookEntity != null) {
            book = new Book();
            book.setRating(bookEntity.getRating());
            book.setSubtitle(bookEntity.getSubtitle());
            book.setAuthor(bookEntity.getAuthor());
            book.setPubdate(bookEntity.getPubdate());
            book.setTags(bookEntity.getTags());
            book.setOrigin_title(bookEntity.getOrigin_title());
            book.setImage(bookEntity.getImage());
            book.setBinding(bookEntity.getBinding());
            book.setTranslator(bookEntity.getTranslator());
            book.setCatalog(bookEntity.getCatalog());
            book.setPages(bookEntity.getPages());
            book.setImages(bookEntity.getImages());
            book.setAlt(bookEntity.getAlt());
            book.setId(bookEntity.getId());
            book.setPublisher(bookEntity.getPublisher());
            book.setIsbn10(bookEntity.getIsbn10());
            book.setIsbn13(bookEntity.getIsbn13());
            book.setTitle(bookEntity.getTitle());
            book.setUrl(bookEntity.getUrl());
            book.setAlt_title(bookEntity.getAlt_title());
            book.setAuthor_intro(bookEntity.getAuthor_intro());
            book.setSummary(bookEntity.getSummary());
            book.setPrice(bookEntity.getPrice());
            book.setStock(bookEntity.getStock());
        }
        return book;
    }

    public List<Book> transform(List<BookEntity> bookEntities) {
        List<Book> bookList = new ArrayList<>();
        Book book;
        for(BookEntity bookEntity : bookEntities) {
            book = this.transform(bookEntity);
            if (book != null) {
                bookList.add(book);
            }
        }

        return bookList;
    }

    public BookEntity transform(Book book) {
        BookEntity bookEntity = null;
        if (book != null) {
            bookEntity = new BookEntity();
            bookEntity.setRating(book.getRating());
            bookEntity.setSubtitle(book.getSubtitle());
            bookEntity.setAuthor(book.getAuthor());
            bookEntity.setPubdate(book.getPubdate());
            bookEntity.setTags(book.getTags());
            bookEntity.setOrigin_title(book.getOrigin_title());
            bookEntity.setImage(book.getImage());
            bookEntity.setBinding(book.getBinding());
            bookEntity.setTranslator(book.getTranslator());
            bookEntity.setCatalog(book.getCatalog());
            bookEntity.setPages(book.getPages());
            bookEntity.setImages(book.getImages());
            bookEntity.setAlt(book.getAlt());
            bookEntity.setId(book.getId());
            bookEntity.setPublisher(book.getPublisher());
            bookEntity.setIsbn10(book.getIsbn10());
            bookEntity.setIsbn13(book.getIsbn13());
            bookEntity.setTitle(book.getTitle());
            bookEntity.setUrl(book.getUrl());
            bookEntity.setAlt_title(book.getAlt_title());
            bookEntity.setAuthor_intro(book.getAuthor_intro());
            bookEntity.setSummary(book.getSummary());
            bookEntity.setPrice(book.getPrice());
        }
        return bookEntity;
    }


}
