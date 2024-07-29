package com.szelestamas.bookstorebackend.api.book.web;

import com.szelestamas.bookstorebackend.api.book.domain.Book;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

public record BookDto(@NotNull String title, @NotNull int price, @NotNull String category, String shortDescription, @Range(min = 0, max = 100) @NotNull int discount,
                      @NotNull boolean available, LocalDate releaseDate, String icon) {
    public Book convertTo() {
        return new Book(0, title, price, category, shortDescription, discount, available, releaseDate, icon);
    }
}
