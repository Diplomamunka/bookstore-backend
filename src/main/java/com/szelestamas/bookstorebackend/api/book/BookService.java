package com.szelestamas.bookstorebackend.api.book;

import com.szelestamas.bookstorebackend.api.author.domain.Author;
import com.szelestamas.bookstorebackend.api.author.persistence.AuthorEntity;
import com.szelestamas.bookstorebackend.api.book.domain.Book;
import com.szelestamas.bookstorebackend.api.book.persistence.BookEntity;
import com.szelestamas.bookstorebackend.api.book.persistence.BookRepository;
import com.szelestamas.bookstorebackend.api.category.domain.Category;
import com.szelestamas.bookstorebackend.api.category.persistence.CategoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    @Value("${upload-path}")
    private String uploadPath;

    public List<Book> getAllBooks() {
        return bookRepository.findAll().stream().map(BookEntity::toBook).toList();
    }

    public Book getBookById(long id) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + id));
        return book.toBook();
    }

    public Book newBook(Book book, Category category, List<Author> authors) {
        BookEntity createdBook = BookEntity.of(book, category, authors);
        BookEntity bookEntity = bookRepository.save(createdBook);
        return bookEntity.toBook();
    }

    public Book updateBook(long id, Book book, Category category, List<Author> authors) {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + id));
        bookEntity.setAvailable(book.available());
        bookEntity.setTitle(book.title());
        bookEntity.setCategory(CategoryEntity.of(category));
        bookEntity.setPrice(book.price());
        bookEntity.setDiscount(book.discount());
        bookEntity.setAuthors(authors.stream().map(AuthorEntity::of).collect(Collectors.toSet()));
        bookEntity.setShortDescription(book.shortDescription());
        bookEntity.setReleaseDate(book.releaseDate());
        return bookRepository.save(bookEntity).toBook();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = IOException.class)
    public void deleteById(long id) throws IOException {
        Optional<BookEntity> book = bookRepository.findById(id);
        if (book.isEmpty())
            return;
        bookRepository.deleteById(id);
        if (book.get().getIcon() != null) {
            Files.delete(Path.of(book.get().getIcon()));
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = IOException.class)
    public void saveFile(InputStream file, long id, String name) throws IOException {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + id));
        if (!Files.exists(Path.of(uploadPath)))
            Files.createDirectories(Path.of(uploadPath));
        Optional<Path> filePath = Files.find(Path.of(uploadPath), 1, (path, attr) ->
            path.getFileName().toString().split("_")[0].equals(Long.toString(id)))
                .findFirst();
        if (filePath.isPresent()) {
            Files.delete(filePath.get());
        }
        String path = uploadPath + "/" + id + "_" + name;
        OutputStream out = new FileOutputStream(path);
        file.transferTo(out);
        bookEntity.setIcon(path);
        bookRepository.save(bookEntity);
        file.close();
        out.close();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = IOException.class)
    public void deleteImage(long id) throws IOException {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + id));
        Optional<Path> filePath = Files.find(Path.of(uploadPath), 1, (path, attr) ->
                        path.getFileName().toString().split("_")[0].equals(Long.toString(id)))
                .findFirst();
        if (filePath.isPresent()) {
            Files.delete(filePath.get());
        }
        bookEntity.setIcon(null);
        bookRepository.save(bookEntity);
    }

    public Resource getFileAsResource(long id) throws MalformedURLException {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + id));
        return new FileUrlResource(book.getIcon());
    }
}
