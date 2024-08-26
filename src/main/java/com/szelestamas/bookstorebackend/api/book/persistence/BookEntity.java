package com.szelestamas.bookstorebackend.api.book.persistence;

import com.szelestamas.bookstorebackend.api.author.domain.Author;
import com.szelestamas.bookstorebackend.api.author.persistence.AuthorEntity;
import com.szelestamas.bookstorebackend.api.book.domain.Book;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
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

    private String category;

    @Column(name = "short_description")
    private String shortDescription;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
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

    public static BookEntity of(Book book) {
        return new BookEntity(book.getId(), book.getTitle(), book.getPrice(), book.getCategory(), book.getShortDescription(),
                book.getAuthors().stream().map(AuthorEntity::of).collect(Collectors.toSet()),
                book.getDiscount(), book.isAvailable(), book.getReleaseDate(), book.getIcon());
    }

    public Book toBook() {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setPrice(price);
        book.setCategory(category);
        book.setShortDescription(shortDescription);
        book.setIcon(icon);
        book.setAvailable(available);
        book.setReleaseDate(releaseDate);
        book.setDiscount(discount);
        ArrayList<Author> authors = new ArrayList<>();
        this.authors.stream().map(author -> {
            Author convertedAuthor = new Author();
            convertedAuthor.setId(author.getId());
            convertedAuthor.setFullName(author.getFullName());
            convertedAuthor.setBooks(new ArrayList<>());
            return convertedAuthor;
        }).forEach(authors::add);
        book.setAuthors(authors);
        return book;
    }

    /*
    public Book toBook() {
        return new Book(id, title, price, category, shortDescription, discount,
                authors.stream().map(AuthorEntity::toAuthor).toList(), available, releaseDate, icon);
    }
     */
}
