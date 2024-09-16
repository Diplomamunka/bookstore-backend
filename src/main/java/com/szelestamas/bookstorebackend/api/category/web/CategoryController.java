package com.szelestamas.bookstorebackend.api.category.web;

import com.szelestamas.bookstorebackend.api.book.BookService;
import com.szelestamas.bookstorebackend.api.book.domain.Book;
import com.szelestamas.bookstorebackend.api.book.web.BookResource;
import com.szelestamas.bookstorebackend.api.category.CategoryService;
import com.szelestamas.bookstorebackend.api.category.domain.Category;
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
@RequestMapping("api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<CategoryResource>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories().stream().map(CategoryResource::of).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResource> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(CategoryResource.of(categoryService.findById(id)));
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookResource>> getBooksByCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getAllBooks(id).stream().map(BookResource::of).toList());
    }

    @PutMapping
    public ResponseEntity<CategoryResource> newCategory(@RequestBody @Valid CategoryDto category) {
        Category createdCategory = categoryService.newCategory(category.convertTo());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdCategory.id()).toUri()).body(CategoryResource.of(createdCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/books")
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
