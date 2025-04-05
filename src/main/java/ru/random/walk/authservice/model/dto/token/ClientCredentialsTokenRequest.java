package ru.random.walk.authservice.model.dto.token;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.random.walk.authservice.model.enam.ClientScope;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
public class ClientCredentialsTokenRequest extends TokenRequest{

    List<ClientScope> scopes;

    @Builder
    public ClientCredentialsTokenRequest(String clientId, List<ClientScope> scopes) {
        super(clientId);
        this.scopes = scopes;
    }
}
