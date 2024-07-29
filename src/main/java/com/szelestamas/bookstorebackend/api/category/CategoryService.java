package com.szelestamas.bookstorebackend.api.category;

import com.szelestamas.bookstorebackend.api.category.domain.Category;
import com.szelestamas.bookstorebackend.api.category.persistence.CategoryEntity;
import com.szelestamas.bookstorebackend.api.category.persistence.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public List<Category> getAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryEntity::toCategory).toList();
    }

    public void deleteByName(String name) {
        categoryRepository.deleteById(name);
    }

    public Category newCategory(Category category) {
        CategoryEntity savedEntity = categoryRepository.save(CategoryEntity.of(category));
        return savedEntity.toCategory();
    }
}
