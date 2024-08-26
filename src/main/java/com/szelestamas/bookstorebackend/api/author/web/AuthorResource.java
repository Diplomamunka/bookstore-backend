package com.szelestamas.bookstorebackend.api.author.web;

import com.szelestamas.bookstorebackend.api.author.domain.Author;
import com.szelestamas.bookstorebackend.api.book.web.BookResource;

import java.util.List;

public record AuthorResource(long id, String fullName, List<BookResource> books) {
    public static AuthorResource of(Author author) {
        return new AuthorResource(author.getId(), author.getFullName(), author.getBooks().stream().map(BookResource::of).toList());
    }
}
