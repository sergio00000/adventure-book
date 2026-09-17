package com.adventure.book.service.impl;

import com.adventure.book.model.Book;
import com.adventure.book.model.enums.Difficulty;
import com.adventure.book.repository.BookRepository;
import com.adventure.book.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> searchBooks(String title, String author, String category, Difficulty difficulty) {
        return bookRepository.findAll()
                .stream()
                .filter(book -> isNull(title) || book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(book -> isNull(author) || book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .filter(book -> isNull(difficulty) || book.getDifficulty().equals(difficulty))
                .filter(book -> isNull(category) || (isNull(book.getCategories())) && book.getCategories().contains(category))
                .toList();
    }

    @Override
    public Optional<Book> findBookById(String id) {
        return bookRepository.findById(id);

    }

    @Override
    public Optional<Book> addCategory(String id, String category) {
        Optional<Book> book = bookRepository.findById(id);
        book.ifPresent(b -> b.getCategories().add(category));

        return book;
    }

    @Override
    public Optional<Book> removeCategory(String id, String category) {
        Optional<Book> book = bookRepository.findById(id);
        book.ifPresent(b -> b.getCategories().remove(category));

        return book;
    }
}
