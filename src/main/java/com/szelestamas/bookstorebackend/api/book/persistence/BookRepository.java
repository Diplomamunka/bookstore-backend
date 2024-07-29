package com.szelestamas.bookstorebackend.api.book.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
}
