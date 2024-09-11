package com.szelestamas.bookstorebackend.api.author.web;

import com.szelestamas.bookstorebackend.api.author.domain.Author;

public record AuthorResource(long id, String fullName) {
    public static AuthorResource of(Author author) {
        return new AuthorResource(author.id(), author.fullName());
    }
}
