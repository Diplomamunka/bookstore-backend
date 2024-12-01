package com.szelestamas.bookstorebackend.core;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorAdvice {
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String noSuchElementHandler(NoSuchElementException ex) {return ex.getMessage();}

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String categoryAlreadyExistsHandler(ResourceAlreadyExistsException ex) {return ex.getMessage();}

    @ExceptionHandler(ResourceCannotBeDeletedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String resourceCannotBeDeletedHandler(ResourceCannotBeDeletedException ex) {return ex.getMessage();}

    @ExceptionHandler(NotEnoughAuthorization.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String notEnoughAuthorizationHandler(NotEnoughAuthorization ex) {return ex.getMessage();}

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String usernameNotFoundHandler(UsernameNotFoundException ex) {return ex.getMessage();}
}
