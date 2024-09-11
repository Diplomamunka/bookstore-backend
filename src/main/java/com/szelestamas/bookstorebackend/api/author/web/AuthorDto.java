package com.szelestamas.bookstorebackend.api.author.web;

import com.szelestamas.bookstorebackend.api.author.domain.Author;
import jakarta.validation.constraints.NotBlank;

public record AuthorDto(@NotBlank String fullName) {
    public Author convertTo() {
        return new Author(null, fullName);
    }
}
