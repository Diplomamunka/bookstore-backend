package com.szelestamas.bookstorebackend.api.category.web;

import com.szelestamas.bookstorebackend.api.category.domain.Category;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CategoryDto(@Length(min = 1, max = 255) @NotNull String name) {
    public static CategoryDto of(Category category) {
        return new CategoryDto(category.name());
    }

    public Category convertTo() {
        return new Category(name);
    }
}
