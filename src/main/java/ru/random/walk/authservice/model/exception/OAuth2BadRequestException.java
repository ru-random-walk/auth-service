package ru.random.walk.authservice.model.exception;

public class OAuth2BadRequestException extends RuntimeException{
    public OAuth2BadRequestException() {
    }

    public OAuth2BadRequestException(String message) {
        super(message);
    }

    public OAuth2BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
