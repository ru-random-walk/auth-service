package ru.random.walk.authservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.random.walk.util.KeyRateLimiter;

import java.time.Duration;
import java.util.UUID;

@Configuration
public class RateLimiterConfig {
    @Bean
    public KeyRateLimiter<String> getUserAvatarRateLimiter(
            @Value("${rate-limiter.getUserAvatarRateLimiter.period-duration}")
            Duration period
    ) {
        return new KeyRateLimiter<>(period);
    }

    @Bean
    public KeyRateLimiter<UUID> uploadUserAvatarRateLimiter(
            @Value("${rate-limiter.uploadUserAvatarRateLimiter.period-duration}")
            Duration period
    ) {
        return new KeyRateLimiter<>(period);
    }
}
