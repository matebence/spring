package com.bence.mate.author.exception;
public class AuthorNotFoundException extends RuntimeException {

    public AuthorNotFoundException() {
        super();
    }

    public AuthorNotFoundException(String message) {
        super(message);
    }

    public AuthorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorNotFoundException(Throwable cause) {
        super(cause);
    }

    protected AuthorNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
