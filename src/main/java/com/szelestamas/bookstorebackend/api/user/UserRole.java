package com.szelestamas.bookstorebackend.api.user;

public enum UserRole {
    ADMIN("ADMIN"),
    STAFF("STAFF"),
    CUSTOMER("CUSTOMER");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getValue() {
        return role;
    }
}
