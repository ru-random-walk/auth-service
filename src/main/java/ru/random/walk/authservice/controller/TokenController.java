package ru.random.walk.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.random.walk.authservice.model.dto.EmailAuthDto;
import ru.random.walk.authservice.model.dto.TokenResponse;
import ru.random.walk.authservice.model.exception.AuthTooManyRequestsException;
import ru.random.walk.authservice.service.facade.TokenFacade;
import ru.random.walk.util.KeyRateLimiter;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TokenController {

    private final TokenFacade tokenFacade;
    private final KeyRateLimiter<String> emailOtpCodeRateLimiter;


    @Operation(description = "OAuth2 token endpoint")
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<TokenResponse> token(
            @Parameter(description = "application/x-www-form-urlencoded request body")
            @RequestParam Map<String, Object> body
    ) {
        String clientId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(tokenFacade.postForToken(body, clientId));
    }

    @Operation(description = "Issue one time password by email")
    @PostMapping(value = "/email/otp")
    public ResponseEntity<Void> issueOtp(@RequestBody EmailAuthDto dto) {
        emailOtpCodeRateLimiter.throwIfRateLimitExceeded(
                dto.email(), () -> new AuthTooManyRequestsException("Limit exceeded for POST /email/otp")
        );
        tokenFacade.issueOneTimePassword(dto);
        return ResponseEntity.ok().build();
    }
}
