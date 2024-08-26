package com.szelestamas.bookstorebackend.api.author.persistence;

import com.szelestamas.bookstorebackend.api.author.domain.Author;
import com.szelestamas.bookstorebackend.api.book.domain.Book;
import com.szelestamas.bookstorebackend.api.book.persistence.BookEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "authors")
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @ManyToMany(mappedBy = "authors")
    private Set<BookEntity> books;

    public static AuthorEntity of(Author author) {
        return new AuthorEntity(author.getId(), author.getFullName(),
                author.getBooks().stream().map(BookEntity::of).collect(Collectors.toSet()));
    }

    public Author toAuthor() {
        Author author = new Author();
        author.setFullName(fullName);
        author.setId(id);
        ArrayList<Book> books = new ArrayList<>();
        this.books.stream().map(book -> {
            Book convertedBook = new Book();
            convertedBook.setId(book.getId());
            convertedBook.setTitle(book.getTitle());
            convertedBook.setCategory(book.getCategory());
            convertedBook.setPrice(book.getPrice());
            convertedBook.setIcon(book.getIcon());
            convertedBook.setAvailable(book.isAvailable());
            convertedBook.setDiscount(book.getDiscount());
            convertedBook.setShortDescription(book.getShortDescription());
            convertedBook.setReleaseDate(book.getReleaseDate());
            convertedBook.setIcon(book.getIcon());
            convertedBook.setAuthors(new ArrayList<>());
            return convertedBook;
        }).forEach(books::add);
        author.setBooks(books);
        return author;
    }

    /*
    public Author toAuthor() {
        return new Author(id, fullName, books.stream().map(BookEntity::toBook).toList());
    }
     */
}
