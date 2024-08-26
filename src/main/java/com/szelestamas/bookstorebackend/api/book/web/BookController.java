package com.szelestamas.bookstorebackend.api.book.web;

import com.szelestamas.bookstorebackend.api.book.BookService;
import com.szelestamas.bookstorebackend.api.book.domain.Book;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookResource>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks().stream().map(BookResource::of).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResource> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(BookResource.of(bookService.getBookById(id)));
    }

    @PutMapping
    public ResponseEntity<BookResource> newBook(@RequestBody @Valid BookDto book) {
        Book createdBook = bookService.newBook(book.convertTo());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdBook.getId()).toUri()).body(BookResource.of(createdBook));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResource> updateBook(@PathVariable Long id, @RequestBody @Valid BookDto book) {
        Book updatedBook = bookService.updateBook(id, book.convertTo());
        return ResponseEntity.ok(BookResource.of(updatedBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok("asd");
    }
}
