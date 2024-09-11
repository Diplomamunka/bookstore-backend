package com.szelestamas.bookstorebackend.api.author.persistence;

import com.szelestamas.bookstorebackend.api.author.domain.Author;
import com.szelestamas.bookstorebackend.api.book.persistence.BookEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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
        return new AuthorEntity(author.id(), author.fullName(), null);
    }

    public Author toAuthor() {
        return new Author(id, fullName);
    }
}
