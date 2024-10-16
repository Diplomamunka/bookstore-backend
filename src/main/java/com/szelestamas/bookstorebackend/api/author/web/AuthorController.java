package com.szelestamas.bookstorebackend.api.author.web;

import com.szelestamas.bookstorebackend.api.author.AuthorService;
import com.szelestamas.bookstorebackend.api.author.domain.Author;
import com.szelestamas.bookstorebackend.api.book.BookService;
import com.szelestamas.bookstorebackend.api.book.domain.Book;
import com.szelestamas.bookstorebackend.api.book.web.BookResource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<AuthorResource>> getAll() {
        return ResponseEntity.ok(authorService.getAllAuthors().stream().map(AuthorResource::of).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResource> findById(@PathVariable Long id) {
        return ResponseEntity.ok(AuthorResource.of(authorService.getAuthorById(id)));
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookResource>> getAllBooks(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAllBooks(id).stream().map(BookResource::of).toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<AuthorResource> newAuthor(@RequestBody @Valid AuthorDto authorDto) {
        Author createdAuthor = authorService.newAuthor(authorDto.convertTo());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdAuthor.id()).toUri()).body(AuthorResource.of(createdAuthor));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<AuthorResource> updateAuthor(@PathVariable Long id, @RequestBody @Valid AuthorDto authorDto) {
        Author updatedAuthor = authorService.update(id, authorDto.convertTo());
        return ResponseEntity.ok(AuthorResource.of(updatedAuthor));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/books")
    @PreAuthorize("hasRole('STAFF')")
    @Transactional
    public ResponseEntity<Void> deleteBooksByAuthor(@PathVariable Long id) {
        List<Book> books = authorService.getAllBooks(id);
        try {
            for (Book book : books) {
                if (book.authors().size() == 1)
                    bookService.deleteById(book.id());
            }
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
