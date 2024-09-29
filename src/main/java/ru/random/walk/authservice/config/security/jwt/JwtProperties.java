package ru.random.walk.authservice.config.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "jwt")
@Configuration
@Data
public class JwtProperties {
    private Long expireTimeInSeconds;
    private String privateKey;
    private String publicKey;
    private Integer refreshTokenExpireTimeInDays;
}
