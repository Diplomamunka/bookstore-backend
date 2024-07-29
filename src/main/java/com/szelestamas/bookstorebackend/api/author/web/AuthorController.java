package com.szelestamas.bookstorebackend.api.author.web;

import com.szelestamas.bookstorebackend.api.author.AuthorService;
import com.szelestamas.bookstorebackend.api.author.domain.Author;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorResource>> getAll() {
        return ResponseEntity.ok(authorService.getAllAuthors().stream().map(AuthorResource::of).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResource> findById(@PathVariable Long id) {
        return new ResponseEntity<>(AuthorResource.of(authorService.getAuthorById(id)), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<AuthorResource> newAuthor(@RequestBody @Valid AuthorDto authorDto) {
        Author createdAuthor = authorService.newAuthor(authorDto.convertTo());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/{id}").buildAndExpand(createdAuthor.id()).toUri()).body(AuthorResource.of(createdAuthor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResource> updateAuthor(@PathVariable Long id, @RequestBody @Valid AuthorDto authorDto) {
        Author updatedAuthor = authorService.update(id, authorDto.convertTo());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/{id}").buildAndExpand(id).toUri()).body(AuthorResource.of(updatedAuthor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
