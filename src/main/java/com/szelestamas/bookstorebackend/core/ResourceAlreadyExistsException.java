package com.szelestamas.bookstorebackend.core;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super("Resource with the following name already exists:" + message);
    }
}
