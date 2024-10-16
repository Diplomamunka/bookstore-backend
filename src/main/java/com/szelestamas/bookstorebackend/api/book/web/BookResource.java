package com.szelestamas.bookstorebackend.api.book.web;

import com.szelestamas.bookstorebackend.api.author.web.AuthorResource;
import com.szelestamas.bookstorebackend.api.book.domain.Book;
import com.szelestamas.bookstorebackend.api.category.web.CategoryResource;

import java.time.LocalDate;
import java.util.List;

public record BookResource(long id, String title, int price, CategoryResource category, String shortDescription, int discount,
                           List<String> tags, List<AuthorResource> authors, boolean available, LocalDate releaseDate) {
    public static BookResource of(Book book) {
        return new BookResource(book.id(), book.title(), book.price(), CategoryResource.of(book.category()), book.shortDescription(),
                book.discount(), book.tags(), book.authors().stream().map(AuthorResource::of).toList(),
                book.available(), book.releaseDate());
    }
}
