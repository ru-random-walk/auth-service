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
            @Parameter(examples = {
                    @ExampleObject(
                            name = "token exchange",
                            value = """
                                            {
                                                "grant_type": "urn:ietf:params:oauth:grant-type:token-exchang",
                                                "subject_token": "ya29.a0AcM612wGZsQ5k3X6MJn4DB8jc8qXXASk4Pw8Q",
                                                "subject_token_type": "Access Token",
                                                "subject_provider": "google"
                                            }
                                    """
                    ),
                    @ExampleObject(
                            name = "refresh token",
                            value = """
                                    {
                                        "grant_type": "refresh_token",
                                        "refresh_token": "07880a4b-1677-41fd-b53d-1401eb8a3802"
                                    }
                                    """
                    )
            })
            @RequestParam Map<String, Object> body
    ) {
        String clientId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(tokenFacade.postForToken(body, clientId));
    }
}
