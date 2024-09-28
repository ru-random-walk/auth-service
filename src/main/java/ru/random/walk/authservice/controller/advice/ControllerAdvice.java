package ru.random.walk.authservice.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.random.walk.authservice.model.dto.ApiErrorDto;
import ru.random.walk.authservice.model.exception.OAuth2BadRequestException;
import ru.random.walk.authservice.model.exception.OAuth2AuthorizationException;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler({OAuth2BadRequestException.class})
    public ResponseEntity<ApiErrorDto> exceptionHandler(OAuth2BadRequestException e) {
        log.warn("Oauth2 exception", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorDto(e.getMessage()));
    }

    @ExceptionHandler({OAuth2AuthorizationException.class})
    public ResponseEntity<ApiErrorDto> exceptionHandler(OAuth2AuthorizationException e) {
        log.warn("Oauth2 exception", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiErrorDto(e.getMessage()));
    }
}
