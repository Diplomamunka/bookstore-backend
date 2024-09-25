package com.szelestamas.bookstorebackend.api.user.web;

import com.szelestamas.bookstorebackend.api.user.UserRole;
import com.szelestamas.bookstorebackend.api.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record SignUpDto(@NotBlank String login,@NotBlank String password,@NotBlank String firstName,@NotBlank String lastName, UserRole role) {
    public User convertTo() {
        return new User(login, new BCryptPasswordEncoder().encode(password), firstName, lastName, role);
    }
}
