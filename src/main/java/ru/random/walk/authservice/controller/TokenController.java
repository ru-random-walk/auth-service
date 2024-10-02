package ru.random.walk.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.random.walk.authservice.model.dto.TokenResponse;
import ru.random.walk.authservice.service.facade.TokenFacade;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TokenController {

    private final TokenFacade tokenFacade;


    @Operation(description = "OAuth2 token endpoint")
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<TokenResponse> token(
            @Parameter(description = "application/x-www-form-urlencoded request body")
            @RequestParam Map<String, Object> body
    ) {
        String clientId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(tokenFacade.postForToken(body, clientId));
    }
}
