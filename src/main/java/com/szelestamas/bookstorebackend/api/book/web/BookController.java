package com.szelestamas.bookstorebackend.api.book.web;

import com.szelestamas.bookstorebackend.api.book.BookService;
import com.szelestamas.bookstorebackend.api.book.domain.Book;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookResource>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks().stream().map(BookResource::of).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResource> getBookById(@PathVariable Long id) {
        return new ResponseEntity<>(BookResource.of(bookService.getBookById(id)), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<BookResource> newBook(@RequestBody @Valid BookDto book) {
        Book createdBook = bookService.newBook(book.convertTo());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/{id}").buildAndExpand(createdBook.id()).toUri()).body(BookResource.of(createdBook));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResource> updateBook(@PathVariable Long id, @RequestBody @Valid BookDto book) {
        Book updatedBook = bookService.updateBook(id, book.convertTo());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/{id}").buildAndExpand(id).toUri()).body(BookResource.of(updatedBook));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
