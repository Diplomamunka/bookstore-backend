package com.szelestamas.bookstorebackend.api.book.domain;

import com.szelestamas.bookstorebackend.api.author.domain.Author;
import com.szelestamas.bookstorebackend.api.category.domain.Category;

import java.time.LocalDate;
import java.util.List;

public record Book(Long id, String title, int price, Category category, String shortDescription, int discount,
                   List<String> tags, List<Author> authors, boolean available, LocalDate releaseDate) {
}
