package ru.random.walk.authservice.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.random.walk.authservice.model.dto.ApiErrorDto;
import ru.random.walk.authservice.model.exception.AuthBadRequestException;
import ru.random.walk.authservice.model.exception.AuthAuthorizationException;
import ru.random.walk.authservice.model.exception.AuthTooManyRequestsException;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler({AuthBadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorDto> exceptionHandler(AuthBadRequestException e) {
        log.warn("Oauth2 exception", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorDto(e.getMessage()));
    }

    @ExceptionHandler({AuthAuthorizationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiErrorDto> exceptionHandler(AuthAuthorizationException e) {
        log.warn("Oauth2 exception", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiErrorDto(e.getMessage()));
    }

    @ExceptionHandler({AuthAuthorizationException.class})
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResponseEntity<ApiErrorDto> exceptionHandler(AuthTooManyRequestsException e) {
        log.warn("Oauth2 exception", e);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new ApiErrorDto(e.getMessage()));
    }
}
