package com.szelestamas.bookstorebackend.api.author.web;

import com.szelestamas.bookstorebackend.api.author.domain.Author;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;

public record AuthorDto(@NotBlank String fullName) {
    public Author convertTo() {
        return new Author(null, fullName, new ArrayList<>());
    }
}
