package com.szelestamas.bookstorebackend.api.user.domain;

import com.szelestamas.bookstorebackend.api.user.UserRole;

public record User(String login, String password, String firstName, String lastName, UserRole role) {
}
