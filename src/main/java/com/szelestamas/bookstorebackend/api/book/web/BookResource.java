package com.szelestamas.bookstorebackend.api.book.web;

import com.szelestamas.bookstorebackend.api.book.domain.Book;

import java.time.LocalDate;

public record BookResource(long id, String title, int price, String category, String shortDescription, int discount, boolean available, LocalDate releaseDate, String icon) {
    public static BookResource of(Book book) {
        return new BookResource(book.id(), book.title(), book.price(), book.category(), book.shortDescription(), book.discount(), book.available(), book.releaseDate(), book.icon());
    }
}
