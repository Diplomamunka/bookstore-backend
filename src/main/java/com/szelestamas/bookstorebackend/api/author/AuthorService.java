package com.szelestamas.bookstorebackend.api.author;

import com.szelestamas.bookstorebackend.api.author.domain.Author;
import com.szelestamas.bookstorebackend.api.author.persistence.AuthorEntity;
import com.szelestamas.bookstorebackend.api.author.persistence.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll().stream().map(AuthorEntity::toAuthor).toList();
    }

    public Author getAuthorById(long id) {
        Optional<AuthorEntity> author = authorRepository.findById(id);
        if (author.isPresent()) {
            return author.get().toAuthor();
        } else {
            throw new NoSuchElementException("Author not found");
        }
    }

    public Author newAuthor(Author author) {
        return authorRepository.save(AuthorEntity.of(author)).toAuthor();
    }

    public void deleteById(long id) {
        authorRepository.deleteById(id);
    }

    public Author update(long id, Author author) {
        Optional<AuthorEntity> authorEntity = authorRepository.findById(id);
        if (authorEntity.isPresent()) {
            AuthorEntity authorEntityToUpdate = authorEntity.get();
            authorEntityToUpdate.setFullName(author.fullName());
            return authorRepository.save(authorEntityToUpdate).toAuthor();
        } else {
            throw new NoSuchElementException("Author not found");
        }
    }
}
