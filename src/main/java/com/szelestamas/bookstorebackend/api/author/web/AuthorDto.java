package com.szelestamas.bookstorebackend.api.author.web;

import com.szelestamas.bookstorebackend.api.author.domain.Author;
import jakarta.validation.constraints.NotNull;

public record AuthorDto(@NotNull String fullName) {
    public Author convertTo() {
        return new Author(0, fullName);
    }
}
