package com.szelestamas.bookstorebackend.api.user.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.szelestamas.bookstorebackend.api.user.UserRole;
import com.szelestamas.bookstorebackend.api.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record SignUpDto(@JsonProperty("email") @NotBlank String login, @NotNull String password, @NotBlank String firstName, @NotBlank String lastName, String role) {
    public User convertTo() {
        return new User(login, null, firstName, lastName, UserRole.valueOf(role.toUpperCase()));
    }

    public User convertWithRole(UserRole role) {
        return new User(login, new BCryptPasswordEncoder().encode(password), firstName, lastName, role);
    }

    public User convertFromUser(User user) {
        return new User(user.login(), new BCryptPasswordEncoder().encode(password), firstName, lastName, user.role());
    }
}
