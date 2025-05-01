package ru.random.walk.authservice.model.exception;

public class AuthAuthorizationException extends RuntimeException {
    public AuthAuthorizationException() {
    }

    public AuthAuthorizationException(String message) {
        super(message);
    }

    public AuthAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
