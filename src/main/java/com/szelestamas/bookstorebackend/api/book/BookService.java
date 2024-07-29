package com.szelestamas.bookstorebackend.api.book;

import com.szelestamas.bookstorebackend.api.book.domain.Book;
import com.szelestamas.bookstorebackend.api.book.persistence.BookEntity;
import com.szelestamas.bookstorebackend.api.book.persistence.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll().stream().map(BookEntity::toBook).toList();
    }

    public Book getBookById(long id) {
        Optional<BookEntity> author = bookRepository.findById(id);
        if (author.isPresent()) {
            return author.get().toBook();
        } else {
            throw new NoSuchElementException("Book not found");
        }
    }

    public Book newBook(Book book) {
        return bookRepository.save(BookEntity.of(book)).toBook();
    }

    public Book updateBook(long id, Book book) {
        Optional<BookEntity> bookEntity = bookRepository.findById(id);
        if (bookEntity.isPresent()) {
            BookEntity bookEntityToUpdate = bookEntity.get();
            bookEntityToUpdate.setAvailable(book.available());
            bookEntityToUpdate.setTitle(book.title());
            bookEntityToUpdate.setCategory(book.category());
            bookEntityToUpdate.setIcon(book.icon());
            bookEntityToUpdate.setPrice(book.price());
            bookEntityToUpdate.setDiscount(book.discount());
            bookEntityToUpdate.setShortDescription(book.shortDescription());
            bookEntityToUpdate.setReleaseDate(book.releaseDate());
            return bookRepository.save(bookEntityToUpdate).toBook();
        } else {
            throw new NoSuchElementException("Author not found");
        }
    }

    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
