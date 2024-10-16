package com.szelestamas.bookstorebackend.core;

public class NotEnoughAuthorization extends RuntimeException {
    public NotEnoughAuthorization(String message) {
        super("You dont have the right to perform this action: " + message);
    }
}
