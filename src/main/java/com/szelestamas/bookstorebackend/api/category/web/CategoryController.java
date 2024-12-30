package com.szelestamas.bookstorebackend.api.category.web;

import com.szelestamas.bookstorebackend.api.book.BookService;
import com.szelestamas.bookstorebackend.api.book.domain.Book;
import com.szelestamas.bookstorebackend.api.book.web.BookResource;
import com.szelestamas.bookstorebackend.api.category.CategoryService;
import com.szelestamas.bookstorebackend.api.category.domain.Category;
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
@RequestMapping("api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<CategoryResource>> getAll() {
        return ResponseEntity.ok(categoryService.getAllCategories().stream().map(CategoryResource::of).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResource> findById(@PathVariable Long id) {
        return ResponseEntity.ok(CategoryResource.of(categoryService.findById(id)));
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookResource>> getAllBooks(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getAllBooks(id).stream().map(BookResource::of).toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<CategoryResource> create(@RequestBody @Valid CategoryDto category) {
        Category createdCategory = categoryService.createCategory(category.convertTo());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdCategory.id()).toUri()).body(CategoryResource.of(createdCategory));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<CategoryResource> update(@PathVariable Long id, @RequestBody @Valid CategoryDto categoryDto) {
        Category updatedCategory = categoryService.updateCategory(id, categoryDto.convertTo());
        return ResponseEntity.ok(CategoryResource.of(updatedCategory));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/books")
    @PreAuthorize("hasRole('STAFF')")
    @Transactional
    public ResponseEntity<Void> deleteBooksByCategory(@PathVariable Long id) {
        List<Book> books = categoryService.getAllBooks(id);
        try {
            for (Book book : books) {
                bookService.deleteById(book.id());
            }
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
