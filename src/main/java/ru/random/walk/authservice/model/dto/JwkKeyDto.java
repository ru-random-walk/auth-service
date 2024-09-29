package ru.random.walk.authservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "JSON object that represents a cryptographic key")
@Builder(toBuilder = true)
public record JwkKeyDto (
        @Schema(description = "The base64url encoded modulus value for the RSA public key")
        String n,
        @Schema(description = "The base64url encoded exponent value for the RSA public key")
        String e,
        @Schema(description = "The kid (key ID) Header Parameter is a hint indicating which key was used to secure the JWS.")
        String kid,
        @Schema(description = "Key type", example = "RSA")
        String kty,
        @Schema(description = "Cryptographic algorithm family used with the key", example = "RS256")
        String alg
) {}
