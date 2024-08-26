package com.szelestamas.bookstorebackend.api.book;

import com.szelestamas.bookstorebackend.api.author.persistence.AuthorEntity;
import com.szelestamas.bookstorebackend.api.author.persistence.AuthorRepository;
import com.szelestamas.bookstorebackend.api.book.domain.Book;
import com.szelestamas.bookstorebackend.api.book.persistence.BookEntity;
import com.szelestamas.bookstorebackend.api.book.persistence.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll().stream().map(BookEntity::toBook).toList();
    }

    public Book getBookById(long id) {
        Optional<BookEntity> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return book.get().toBook();
        } else {
            throw new NoSuchElementException("Book not found");
        }
    }

    public Book newBook(Book book) {
        BookEntity bookEntity = bookRepository.save(BookEntity.of(book));
        manageAuthors(bookEntity);
        return bookEntity.toBook();
    }

    public Book updateBook(long id, Book book) {
        Optional<BookEntity> bookEntity = bookRepository.findById(id);
        if (bookEntity.isPresent()) {
            BookEntity bookEntityToUpdate = bookEntity.get();
            bookEntityToUpdate.setAvailable(book.isAvailable());
            bookEntityToUpdate.setTitle(book.getTitle());
            bookEntityToUpdate.setCategory(book.getCategory());
            bookEntityToUpdate.setIcon(book.getIcon());
            bookEntityToUpdate.setPrice(book.getPrice());
            bookEntityToUpdate.setDiscount(book.getDiscount());
            bookEntityToUpdate.setAuthors(book.getAuthors().stream().map(AuthorEntity::of).collect(Collectors.toSet()));
            bookEntityToUpdate.setShortDescription(book.getShortDescription());
            bookEntityToUpdate.setReleaseDate(book.getReleaseDate());
            manageAuthors(bookEntityToUpdate);
            return bookRepository.save(bookEntityToUpdate).toBook();
        } else {
            throw new NoSuchElementException("Book not found");
        }
    }

    public void deleteById(long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(NoSuchElementException::new);
        Set<AuthorEntity> authors = bookEntity.getAuthors();
        for (AuthorEntity author : authors) {
            Set<BookEntity> books = author.getBooks();
            books.remove(bookEntity);
            author.setBooks(books);
        }
        authorRepository.saveAll(authors);
        bookRepository.deleteById(id);
    }

    private void manageAuthors(BookEntity bookEntity) {
        for (AuthorEntity author : bookEntity.getAuthors()) {
            Optional<AuthorEntity> authorEntity = authorRepository.findByFullName(author.getFullName());
            if (authorEntity.isEmpty()) {
                authorRepository.save(author);
            } else {
                Set<BookEntity> books = authorEntity.get().getBooks();
                books.add(bookEntity);
                authorEntity.get().setBooks(books);
                authorRepository.save(author);
            }
        }
    }
}
