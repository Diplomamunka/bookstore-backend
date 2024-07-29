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
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories().stream().map(CategoryDto::of).toList());
    }

    @PutMapping
    public ResponseEntity<CategoryDto> newCategory(@RequestBody @Valid CategoryDto category) {
        Category createdCategory = categoryService.newCategory(category.convertTo());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/{name}").buildAndExpand(createdCategory.name()).toUri()).body(CategoryDto.of(createdCategory));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String name) {
        categoryService.deleteByName(name);

        return ResponseEntity.ok().build();
    }
}
