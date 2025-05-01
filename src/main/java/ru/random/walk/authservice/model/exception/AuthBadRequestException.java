package ru.random.walk.authservice.model.exception;

public class AuthBadRequestException extends RuntimeException{
    public AuthBadRequestException() {
    }

    public AuthBadRequestException(String message) {
        super(message);
    }

    public AuthBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
