package ru.random.walk.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import ru.random.walk.authservice.service.client.GoogleAuthClient;
import ru.random.walk.authservice.service.client.YandexAuthClient;

@Configuration
public class WebClientConfig {

    @Bean
    public GoogleAuthClient googleAuthClient(AuthServiceProperties properties) {
        var client = WebClient.builder()
                .baseUrl(properties.getGoogle().getGoogleAuthUrl())
                .build();
        var httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(client))
                .build();
        return httpServiceProxyFactory.createClient(GoogleAuthClient.class);
    }

    @Bean
    public YandexAuthClient yandexAuthClient(AuthServiceProperties properties) {
        var client = WebClient.builder()
                .baseUrl(properties.getYandex().getYandexAuthUrl())
                .build();
        var httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(client))
                .build();
        return httpServiceProxyFactory.createClient(YandexAuthClient.class);
    }
}
