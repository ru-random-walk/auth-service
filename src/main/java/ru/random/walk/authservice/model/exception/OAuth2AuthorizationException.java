package ru.random.walk.authservice.model.exception;

public class OAuth2AuthorizationException extends RuntimeException {
    public OAuth2AuthorizationException() {
    }

    public OAuth2AuthorizationException(String message) {
        super(message);
    }

    public OAuth2AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
