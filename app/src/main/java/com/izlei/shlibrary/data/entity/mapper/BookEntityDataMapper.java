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


}
