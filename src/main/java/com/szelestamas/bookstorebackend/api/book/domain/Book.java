package com.szelestamas.bookstorebackend.api.book.domain;

import java.time.LocalDate;

public record Book(long id, String title, int price, String category, String shortDescription, int discount, boolean available, LocalDate releaseDate, String icon) {
}
