package ru.random.walk.authservice.service.client;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import ru.random.walk.authservice.model.dto.clients.YandexUserInfoDto;

public interface YandexAuthClient {
    @GetExchange(url = "/info")
    YandexUserInfoDto getUserInfo(
            @RequestHeader("Authorization") String token,
            @RequestParam("format") String returnFormat
    );
}
