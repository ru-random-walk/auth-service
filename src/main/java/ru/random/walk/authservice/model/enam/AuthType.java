package ru.random.walk.authservice.model.enam;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.random.walk.authservice.model.exception.OAuth2BadRequestException;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum AuthType {
    PASSWORD("password"),
    GOOGLE("google"),
    YANDEX("yandex");

    private final String name;

    public static AuthType getByName(String name) {
        return Arrays.stream(AuthType.values())
                .filter(type -> type.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new OAuth2BadRequestException("There is no provider type with name: " + name));
    }
}
