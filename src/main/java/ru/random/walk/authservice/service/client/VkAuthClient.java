package ru.random.walk.authservice.service.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import ru.random.walk.authservice.model.dto.clients.VkUserInfoDto;

public interface VkAuthClient {
    @GetExchange(url = "/user_info")
    VkUserInfoDto getUserInfo(
            @RequestParam(name = "client_id") String clientId,
            @RequestParam(name = "access_token") String accessToken
    );
}
