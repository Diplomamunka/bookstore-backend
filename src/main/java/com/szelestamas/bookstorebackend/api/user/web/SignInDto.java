package com.szelestamas.bookstorebackend.api.user.web;

import com.szelestamas.bookstorebackend.api.user.domain.User;
import jakarta.validation.constraints.NotBlank;

public record SignInDto(@NotBlank String login,@NotBlank String password) {
    public User convertTo() {
        return new User(login, password, null, null, null);
    }
}
