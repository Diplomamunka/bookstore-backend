package com.szelestamas.bookstorebackend.api.book.persistence;

import com.szelestamas.bookstorebackend.api.author.domain.Author;
import com.szelestamas.bookstorebackend.api.author.persistence.AuthorEntity;
import com.szelestamas.bookstorebackend.api.book.domain.Book;
import com.szelestamas.bookstorebackend.api.category.domain.Category;
import com.szelestamas.bookstorebackend.api.category.persistence.CategoryEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int price;

    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "name")
    private CategoryEntity category;

    @Column(name = "short_description")
    private String shortDescription;

    @ManyToMany
    @JoinTable(
            name = "books_authors",
            joinColumns = { @JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")}
    )
    private Set<AuthorEntity> authors;

    private int discount;

    @Column(name = "is_available")
    private boolean available;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    private String icon;

    public static BookEntity of(Book book, Category category, List<Author> authors) {
        return new BookEntity(book.id(), book.title(), book.price(), CategoryEntity.of(category), book.shortDescription(),
                authors.stream().map(AuthorEntity::of).collect(Collectors.toSet()),
                book.discount(), book.available(), book.releaseDate(), null);
    }

    public Book toBook() {
        return new Book(id, title, price, category.toCategory(), shortDescription, discount,
                authors.stream().map(AuthorEntity::toAuthor).toList(), available, releaseDate);
    }
}
