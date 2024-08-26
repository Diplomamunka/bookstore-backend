package com.szelestamas.bookstorebackend.api.author.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    Optional<AuthorEntity> findByFullName(String fullName);
}
