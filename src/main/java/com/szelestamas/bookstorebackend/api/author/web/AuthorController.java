package com.szelestamas.bookstorebackend.api.author.web;

import com.szelestamas.bookstorebackend.api.author.AuthorService;
import com.szelestamas.bookstorebackend.api.author.domain.Author;
import com.szelestamas.bookstorebackend.api.book.BookService;
import com.szelestamas.bookstorebackend.api.book.web.BookResource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PutMapping
    public ResponseEntity<AuthorResource> newAuthor(@RequestBody @Valid AuthorDto authorDto) {
        Author createdAuthor = authorService.newAuthor(authorDto.convertTo());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdAuthor.id()).toUri()).body(AuthorResource.of(createdAuthor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResource> updateAuthor(@PathVariable Long id, @RequestBody @Valid AuthorDto authorDto) {
        Author updatedAuthor = authorService.update(id, authorDto.convertTo());
        return ResponseEntity.ok(AuthorResource.of(updatedAuthor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/books")
    @Transactional
    public ResponseEntity<Map<String, Object>> deleteBooksByAuthor(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        List<Long> failedDeletes = new ArrayList<>();
        List<Long> successfulDeletes = new ArrayList<>();
        authorService.getAllBooks(id).forEach(book -> {
            try {
                if (book.authors().size() == 1) {
                    bookService.deleteById(book.id());
                    successfulDeletes.add(book.id());
                }
            } catch (IOException ignored) {
                failedDeletes.add(book.id());
            }
        });
        if (failedDeletes.isEmpty())
            return ResponseEntity.noContent().build();
        response.put("message", "Some books could not be deleted");
        response.put("failedDeletes", failedDeletes);
        response.put("successfulDeletes", successfulDeletes);
        return ResponseEntity.internalServerError().body(response);
    }
}
