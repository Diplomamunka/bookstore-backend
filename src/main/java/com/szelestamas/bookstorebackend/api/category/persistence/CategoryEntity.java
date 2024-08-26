package com.szelestamas.bookstorebackend.api.category.persistence;

import com.szelestamas.bookstorebackend.api.category.domain.Category;
import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public static CategoryEntity of(Category category) {
        return new CategoryEntity(category.id(), category.name());
    }

    public Category toCategory() {
        return new Category(id, name);
    }
}
