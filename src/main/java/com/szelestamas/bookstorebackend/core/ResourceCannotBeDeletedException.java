package com.szelestamas.bookstorebackend.core;

public class ResourceCannotBeDeletedException extends RuntimeException {
    public ResourceCannotBeDeletedException(String message) {
        super(message);
    }
}
