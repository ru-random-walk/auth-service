package ru.random.walk.authservice.service.oauth2.exchange;

import ru.random.walk.authservice.model.enam.AuthType;
import ru.random.walk.authservice.model.entity.AuthUser;

public interface AccessTokenExchanger {
    boolean supports(AuthType subjectProviderType);
    AuthUser exchange(String subjectToken);
}
