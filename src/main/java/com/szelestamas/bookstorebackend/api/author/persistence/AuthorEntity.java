package com.szelestamas.bookstorebackend.api.author.persistence;

import com.szelestamas.bookstorebackend.api.author.domain.Author;
import com.szelestamas.bookstorebackend.api.author.web.AuthorDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "authors")
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "full_name")
    private String fullName;

    public static AuthorEntity of(Author author) {
        return new AuthorEntity(author.id(), author.fullName());
    }

    public Author toAuthor() {
        return new Author(id, fullName);
    }
}
