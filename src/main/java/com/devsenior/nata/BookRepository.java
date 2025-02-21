package com.devsenior.nata;

import com.devsenior.nata.model.Book;

public interface BookRepository {
    void save(Book book);
    Book findById(String id);
}
