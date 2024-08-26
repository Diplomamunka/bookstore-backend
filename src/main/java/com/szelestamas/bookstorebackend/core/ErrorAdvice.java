package com.szelestamas.bookstorebackend.core;

import com.szelestamas.bookstorebackend.api.book.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorAdvice {
    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String bookNotFoundHandler(BookNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String noSuchElementHandler(NoSuchElementException ex) {return ex.getMessage();}

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String categoryAlreadyExistsHandler(ResourceAlreadyExistsException ex) {return ex.getMessage();}

    @ExceptionHandler(ResourceCannotBeDeletedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String resourceCannotBeDeletedHandler(ResourceCannotBeDeletedException ex) {return ex.getMessage();}
}
