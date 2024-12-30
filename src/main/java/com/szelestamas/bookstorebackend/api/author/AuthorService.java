package com.szelestamas.bookstorebackend.api.author;

import com.szelestamas.bookstorebackend.api.author.domain.Author;
import com.szelestamas.bookstorebackend.api.author.persistence.AuthorEntity;
import com.szelestamas.bookstorebackend.api.author.persistence.AuthorRepository;
import com.szelestamas.bookstorebackend.api.book.domain.Book;
import com.szelestamas.bookstorebackend.api.book.persistence.BookEntity;
import com.szelestamas.bookstorebackend.core.ResourceAlreadyExistsException;
import com.szelestamas.bookstorebackend.core.ResourceCannotBeDeletedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
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
        AuthorEntity author = authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author not found with id: " + id));
        return author.toAuthor();
    }

    public List<Book> getAllBooks(long id) {
        AuthorEntity author = authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author not found with id: " + id));
        return author.getBooks().stream().map(BookEntity::toBook).toList();
    }

    public Author findByFullName(String name) {
        AuthorEntity author = authorRepository.findByFullName(name)
                .orElseThrow(() -> new NoSuchElementException("Author not found: " + name));

        return author.toAuthor();
    }

    public Author createAuthor(Author author) {
        if (authorRepository.findByFullName(author.fullName()).isPresent())
            throw new ResourceAlreadyExistsException(author.fullName());
        return authorRepository.save(AuthorEntity.of(author)).toAuthor();
    }

    public Author updateAuthor(long id, Author author) {
        AuthorEntity authorEntity = authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author not found with id: " + id));
        if (authorRepository.exists(Example.of(AuthorEntity.of(author))))
            throw new ResourceAlreadyExistsException("Author already exists with the name: " + author.fullName());
        authorEntity.setFullName(author.fullName());
        return authorRepository.save(authorEntity).toAuthor();
    }

    public void deleteById(long id) {
        Optional<AuthorEntity> author = authorRepository.findById(id);
        if (author.isEmpty())
            return;
        if (!author.get().getBooks().isEmpty())
            throw new ResourceCannotBeDeletedException("Author still have books, cannot delete it.");
        authorRepository.deleteById(id);
    }
}
