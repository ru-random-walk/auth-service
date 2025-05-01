package ru.random.walk.authservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "auth")
@Configuration
@Data
public class AuthServiceProperties {

    private String issuerUrl;
    private Google google;
    private Yandex yandex;

    @Data
    public static class Google {
        private String googleAuthUrl;
    }

    @Data
    public static class Yandex {
        private String yandexAuthUrl;
    }
}
