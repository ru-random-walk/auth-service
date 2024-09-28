package ru.random.walk.authservice.model.dto;

import lombok.Builder;

/**
 *
 * @param n base64url-encoded значение модуля RSA public key
 * @param e base64url-encoded значение экспоненты RSA public key
 * @param kid The kid (key ID) Header Parameter is a hint indicating which key was used to secure the JWS.
 *            This parameter allows originators to explicitly signal a change of key to recipients.
 *            The structure of the kid value is unspecified. Its value MUST be a case-sensitive string.
 * @param kty тип ключа
 * @param alg алгоритм шифрования
 */
@Builder(toBuilder = true)
public record JwkKeyDto (
    String n,
    String e,
    String kid,
    String kty,
    String alg
) {}
