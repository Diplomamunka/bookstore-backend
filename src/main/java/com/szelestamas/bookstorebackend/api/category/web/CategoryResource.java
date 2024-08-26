package com.szelestamas.bookstorebackend.api.category.web;

import com.szelestamas.bookstorebackend.api.category.domain.Category;

public record CategoryResource(long id, String name) {
    public static CategoryResource of(Category category) {
        return new CategoryResource(category.id(), category.name());
    }
}
