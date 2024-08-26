package com.szelestamas.bookstorebackend.api.author.domain;

import com.szelestamas.bookstorebackend.api.book.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    private Long id;
    private String fullName;
    private List<Book> books;
}
