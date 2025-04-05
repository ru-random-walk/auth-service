package ru.random.walk.authservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.model.enam.ClientScope;
import ru.random.walk.authservice.model.entity.OAuthClient;
import ru.random.walk.authservice.model.entity.OAuthScope;
import ru.random.walk.authservice.repository.OAuthClientRepository;
import ru.random.walk.authservice.service.OAuthClientService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuthClientServiceImpl implements OAuthClientService {

    private final OAuthClientRepository clientRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var client = getClientById(username);
        return User.builder()
                .username(client.getClientId())
                .password(client.getClientSecret())
                .authorities(client.getAuthorities())
                .build();
    }

    @Override
    public List<ClientScope> getScopesById(String clientId) {
        var client = getClientById(clientId);
        return client.getScopes().stream()
                .map(OAuthScope::getName)
                .toList();
    }

    private OAuthClient getClientById(String clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new UsernameNotFoundException("Unknown clientId"));
    }
}
