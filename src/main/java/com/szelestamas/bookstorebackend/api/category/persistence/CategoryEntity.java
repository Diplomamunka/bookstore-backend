package com.szelestamas.bookstorebackend.api.category.persistence;

import com.szelestamas.bookstorebackend.api.category.domain.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class CategoryEntity {
    @Id
    private String name;

    public static CategoryEntity of(Category category) {
        return new CategoryEntity(category.name());
    }

    public Category toCategory() {
        return new Category(name);
    }
}
