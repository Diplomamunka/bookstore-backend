package com.szelestamas.bookstorebackend.api.book.domain;

import com.szelestamas.bookstorebackend.api.author.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private Long id;
    private String title;
    private int price;
    private String category;
    private String shortDescription;
    private int discount;
    private List<Author> authors;
    private boolean available;
    private LocalDate releaseDate;
    private String icon;
}
