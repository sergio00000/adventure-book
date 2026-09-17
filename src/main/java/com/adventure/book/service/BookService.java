package com.adventure.book.service;

import com.adventure.book.model.Book;
import com.adventure.book.model.enums.Difficulty;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> searchBooks(String title, String author, String category, Difficulty difficulty);

    Optional<Book> findBookById(String id);

    Optional<Book> addCategory(String id, String category);

    Optional<Book> removeCategory(String id, String category);
}
