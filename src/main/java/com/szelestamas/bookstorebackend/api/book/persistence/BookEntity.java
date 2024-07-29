package com.szelestamas.bookstorebackend.api.book.persistence;

import com.szelestamas.bookstorebackend.api.book.domain.Book;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private int price;

    private String category;

    @Column(name = "short_description")
    private String shortDescription;

    private int discount;

    @Column(name = "is_available")
    private boolean available;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    private String icon;

    public static BookEntity of(Book book) {
        return new BookEntity(0, book.title(), book.price(), book.category(), book.shortDescription(), book.discount(), book.available(), book.releaseDate(), book.icon());
    }

    public Book toBook() {
        return new Book(id, title, price, category, shortDescription, discount, available, releaseDate, icon);
    }
}
