package com.szelestamas.bookstorebackend.api.book.web;

import com.szelestamas.bookstorebackend.api.author.AuthorService;
import com.szelestamas.bookstorebackend.api.author.domain.Author;
import com.szelestamas.bookstorebackend.api.author.web.AuthorDto;
import com.szelestamas.bookstorebackend.api.book.BookService;
import com.szelestamas.bookstorebackend.api.book.domain.Book;
import com.szelestamas.bookstorebackend.api.category.CategoryService;
import com.szelestamas.bookstorebackend.api.category.domain.Category;
import com.szelestamas.bookstorebackend.api.user.AuthService;
import com.szelestamas.bookstorebackend.api.user.domain.User;
import com.szelestamas.bookstorebackend.api.user.persistence.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<BookResource>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks().stream().map(BookResource::of).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResource> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(BookResource.of(bookService.getBookById(id)));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getImage(@PathVariable Long id) {
        Resource resource;
        String contentType;
        try {
            resource = bookService.getFileAsResource(id);
            contentType = Files.probeContentType(Path.of(resource.getFilename()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename()
                            .substring(resource.getFilename().indexOf('_') + 1) + "\"")
                    .contentType(MediaType.parseMediaType(contentType)).body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<BookResource> newBook(@RequestBody @Valid BookDto book) {
        List<Author> authors = findOrCreateAuthors(book.authors());
        Category category = categoryService.findByName(book.category().name());
        Book createdBook = bookService.newBook(book.convertTo(), category, authors);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdBook.id()).toUri()).body(BookResource.of(createdBook));
    }

    @PostMapping("{id}/bookmark")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<BookResource>> addBookmark(@PathVariable Long id, @AuthenticationPrincipal UserEntity authenticatedPrincipal) {
        User user =  authenticatedPrincipal.toUser();
        Book book = bookService.getBookById(id);
        Category category = categoryService.findByName(book.category().name());
        List<Book> books = authService.addBookmark(user.login(), book, category, book.authors());
        return ResponseEntity.ok(books.stream().map(BookResource::of).toList());
    }

    @PostMapping("/{id}/image")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        if (!isFileContentIsGood(file.getContentType())) {
            return ResponseEntity.badRequest().body("Invalid file type. Only JPEG, PNG, SVG and WEBP files are allowed.");
        }
        try {
            bookService.saveFile(file.getInputStream(), id, file.getOriginalFilename());
            return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<BookResource> updateBook(@PathVariable Long id, @RequestBody @Valid BookDto book) {
        List<Author> authors = findOrCreateAuthors(book.authors());
        Category category = categoryService.findByName(book.category().name());
        Book updatedBook = bookService.updateBook(id, book.convertTo(), category, authors);
        return ResponseEntity.ok(BookResource.of(updatedBook));
    }

    @DeleteMapping("{id}/bookmark")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long id, @AuthenticationPrincipal UserEntity authenticatedPrincipal) {
        User user =  authenticatedPrincipal.toUser();
        authService.deleteBookmark(user.login(), id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}/image")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        try {
            bookService.deleteImage(id);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean isFileContentIsGood(String contentType) {
        return contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/svg+xml")
                || contentType.equals("image/webp");
    }

    private List<Author> findOrCreateAuthors(List<AuthorDto> authors) {
        return authors.stream().map(author -> {
            try {
                return authorService.findByFullName(author.fullName());
            } catch (NoSuchElementException e) {
                return authorService.newAuthor(author.convertTo());
            }
        }).toList();
    }
}
