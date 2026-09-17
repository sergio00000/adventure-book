package com.adventure.book.controller;

import com.adventure.book.controller.dto.CategoryRequest;
import com.adventure.book.model.Book;
import com.adventure.book.model.enums.Difficulty;
import com.adventure.book.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> searchBooks(@RequestParam(required = false) String title,
                                  @RequestParam(required = false) String author,
                                  @RequestParam(required = false) String category,
                                  @RequestParam(required = false) Difficulty difficulty) {
        return bookService.searchBooks(title, author, category, difficulty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        return bookService.findBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/categories")
    public ResponseEntity<Book> addCategory(@PathVariable String id, @RequestBody CategoryRequest request) {
        return bookService.addCategory(id, request.category())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/categories/{category}")
    public ResponseEntity<Book> removeCategory(@PathVariable String id, @PathVariable String category) {
        return bookService.removeCategory(id, category)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
