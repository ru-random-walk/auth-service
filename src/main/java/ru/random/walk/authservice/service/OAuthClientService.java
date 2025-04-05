package ru.random.walk.authservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.random.walk.authservice.model.enam.ClientScope;

import java.util.List;

public interface OAuthClientService extends UserDetailsService {
    List<ClientScope> getScopesById(String clientId);
}
