package com.szelestamas.bookstorebackend.api.category;

import com.szelestamas.bookstorebackend.api.author.persistence.AuthorEntity;
import com.szelestamas.bookstorebackend.api.book.domain.Book;
import com.szelestamas.bookstorebackend.api.book.persistence.BookEntity;
import com.szelestamas.bookstorebackend.api.category.domain.Category;
import com.szelestamas.bookstorebackend.api.category.persistence.CategoryEntity;
import com.szelestamas.bookstorebackend.api.category.persistence.CategoryRepository;
import com.szelestamas.bookstorebackend.core.ResourceAlreadyExistsException;
import com.szelestamas.bookstorebackend.core.ResourceCannotBeDeletedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryEntity::toCategory).toList();
    }

    public List<Book> getAllBooks(long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found with id: " + id));
        return category.getBooks().stream().map(BookEntity::toBook).toList();
    }

    public void deleteById(long id) {
        Optional<CategoryEntity> category = categoryRepository.findById(id);
        if (category.isEmpty())
            return;
        if (!category.get().getBooks().isEmpty())
            throw new ResourceCannotBeDeletedException("Category still have books, cannot delete it.");
        categoryRepository.deleteById(id);
    }

    public Category newCategory(Category category) {
        if (categoryRepository.findByName(category.name()).isPresent())
            throw new ResourceAlreadyExistsException(category.name());
        CategoryEntity savedEntity = categoryRepository.save(CategoryEntity.of(category));
        return savedEntity.toCategory();
    }

    public Category findById(long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found with id: " + id));
        return category.toCategory();
    }

    public Category findByName(String name) {
        CategoryEntity category = categoryRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Category not found: " + name));
        return category.toCategory();
    }
}
