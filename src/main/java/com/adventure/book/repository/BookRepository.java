package com.adventure.book.repository;

import com.adventure.book.model.Book;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BookRepository {

    private final Map<String, Book> repository;

    public BookRepository() {
        this.repository = new HashMap<>();
    }

    public void save(Book book) {
        repository.put(book.getId(), book);
    }

    public Optional<Book> findById(String id) {
        return Optional.ofNullable(repository.get(id));
    }

    public List<Book> findAll() {
        return new ArrayList<>(repository.values());
    }
}
