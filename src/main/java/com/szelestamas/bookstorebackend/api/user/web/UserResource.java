package com.szelestamas.bookstorebackend.api.user.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.szelestamas.bookstorebackend.api.user.UserRole;
import com.szelestamas.bookstorebackend.api.user.domain.User;

public record UserResource(@JsonProperty("email") String login, String firstName, String lastName, UserRole role) {
    public static UserResource of(User user) {
        return new UserResource(user.login(), user.firstName(), user.lastName(), user.role());
    }
}
