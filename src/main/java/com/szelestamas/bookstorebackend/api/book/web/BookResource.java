package com.szelestamas.bookstorebackend.api.book.web;

import com.szelestamas.bookstorebackend.api.author.web.AuthorResource;
import com.szelestamas.bookstorebackend.api.book.domain.Book;

import java.time.LocalDate;
import java.util.List;

public record BookResource(long id, String title, int price, String category, String shortDescription, int discount,
                           List<AuthorResource> authors, boolean available, LocalDate releaseDate, String icon) {
    public static BookResource of(Book book) {
        return new BookResource(book.getId(), book.getTitle(), book.getPrice(), book.getCategory(), book.getShortDescription(),
                book.getDiscount(), book.getAuthors().stream().map(AuthorResource::of).toList(),
                book.isAvailable(), book.getReleaseDate(), book.getIcon());
    }
}
