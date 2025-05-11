package ru.random.walk.authservice.model.exception;

public class AuthTooManyRequestsException extends RuntimeException {
    public AuthTooManyRequestsException() {
    }

    public AuthTooManyRequestsException(String message) {
        super(message);
    }
}
