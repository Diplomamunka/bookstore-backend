package com.szelestamas.bookstorebackend.api.book.web;

import com.szelestamas.bookstorebackend.api.author.web.AuthorDto;
import com.szelestamas.bookstorebackend.api.book.domain.Book;
import com.szelestamas.bookstorebackend.api.category.web.CategoryDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.util.List;

public record BookDto(@NotBlank String title, @NotNull int price, @NotNull CategoryDto category, String shortDescription,
                      @Range(min = 0, max = 100) @NotNull int discount,
                      @NotEmpty List<AuthorDto> authors,
                      @NotNull boolean available, LocalDate releaseDate) {
    public Book convertTo() {
        return new Book(null, title, price, category.convertTo(), shortDescription,
                discount, authors.stream().map(AuthorDto::convertTo).toList(), available, releaseDate);
    }
}
