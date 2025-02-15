package ru.random.walk.authservice.service.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import ru.random.walk.authservice.model.dto.GoogleUserInfoDto;

public interface GoogleAuthClient {

    @GetExchange(url = "/userinfo/v2/me")
    GoogleUserInfoDto getUserInfo(@RequestParam(name = "access_token") String accessToken);

}
