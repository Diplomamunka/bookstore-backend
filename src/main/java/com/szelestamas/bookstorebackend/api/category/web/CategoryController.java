package com.szelestamas.bookstorebackend.api.category.web;

import com.szelestamas.bookstorebackend.api.category.CategoryService;
import com.szelestamas.bookstorebackend.api.category.domain.Category;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResource>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories().stream().map(CategoryResource::of).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResource> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(CategoryResource.of(categoryService.findById(id)));
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
}
