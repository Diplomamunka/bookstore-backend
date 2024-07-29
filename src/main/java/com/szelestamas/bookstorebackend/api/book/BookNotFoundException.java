package com.szelestamas.bookstorebackend.api.book;

public class BookNotFoundException extends RuntimeException {
    BookNotFoundException(Long id) {
        super("Book not found: " + id);
    }
}
