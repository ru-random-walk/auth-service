package ru.random.walk.authservice.model.exception;

public class AuthNotFoundException extends RuntimeException{
    public AuthNotFoundException() {
    }

    public AuthNotFoundException(String message) {
        super(message);
    }

    public AuthNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthNotFoundException(Throwable cause) {
        super(cause);
    }

    public AuthNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
