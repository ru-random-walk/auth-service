# auth-service
Отвечает за авторизацию и аутентификацию клиентов и api(тоже выступает клиентом), хранит информацию, необходимую для авторизации пользователя, а также список их привелегий

---
## Функциональные требования: ##

- Поддержка регистрации (login + password)
- Поддержка логина(login + password)
- Поддержка логина и регистрации через сторонние апи по OAuth2.0 (google, vk и тп)
- Авторизация через jwt токены
- Jwt токены должны быть «обновляемыми», то есть должна быть поддержка refresh token’ов
- Пароли должны храниться в зашифрованном виде в базе данных
---
## Доменная модель: ##

<img width="752" alt="image" src="https://github.com/user-attachments/assets/87d1abd4-2121-4503-b923-a79af9a8d5c1">

---
## Авторизация клиента ##

Для обращения к закрытому api сервиса нужно обращаться к нему в качестве OAuth клиента. В качестве клиента может выступать другой сервис или приложение. Пользователи приложений обращаются к нему под учетной записью клиента. На текущий момент клиенты заводятся руками разработчиками напрямую через БД.
Credentials клиента передаются в заголовке запроса Authorization в формате Basic Auth.

---
## API сервиса авторизации ##

### _POST /token_ - эндпоинт для получения токенов доступа до Random Walk API 

**Выходные данные** в формате _application/json_:
- access_token = jwt токен доступа
- refresh_token = токен для обновления access_token
- token_type = Тип access_token (Bearer)
- expires_in = Время до истечения токена в секундах

**Входные данные** различаются в зависимости от переданного grant_type.

---

### Grant type urn:ietf:params:oauth:grant-type:token-exchange ###

Через данный grant_type осуществляется обмен токена из сторонней системы на токен для Random Walk API

**Принимает:**
- Credentials клиента в заголовке Authorization в формате Basic Auth
- Тело запроса в формате _application/x-www-form-urlencoded_:
  - grant_type (required) = urn:ietf:params:oauth:grant-type:token-exchange
  - subject_token (required) = Токен доступа из стороннего источника
  - subject_token_type (required) = Тип токена (На текущий момент поддерживается только Access Token)
  - subject_token_provider (required) = Название платформы в lowercase, которой пренадлежит предоставляемый токен доступа (google, yandex, etc.)

---

### Grant type refresh_token ###

Через данный grant_type происходит обновление токена доступа через refresh_token

**Принимает:**
- Credentials клиента в заголовке Authorization в формате Basic Auth
- Тело запроса в формате _application/x-www-form-urlencoded_:
  - grant_type (required) = refresh_token
  - refresh_token (required) = значение полученного ранее refresh_token

---

### GET /.well-known/openid-configuration 
Получить метаданные сервера авторизации. Список урл ручек из стандарта OAuth и поддерживаемых grant_type
**Пример ответа:**
```
{
    "issuer": "https://issuer-url.com/auth",               # url of issuer
    "token_endpoint": "https://issuer-url.com/auth/token", # url of token endpoint
    "jwks_uri": "https://issuer-url.com/auth/jwks",        # url of jwk information endpoint
    "grant_types_supported": [                            # supported grant types
        "urn:ietf:params:oauth:grant-type:token-exchange",
        "refresh_token"
    ],
    "response_types_supported": [                         # supported response types
        "token"
    ]
}
```

---

### GET /jwks 
Получить информацию о JWK публичного ключа. Используется ресурсными серверами для получения публичного ключа для валидации получаемых jwt токенов

---
## Флоу авторизации и получения доступа к ресурсу через token exchange

![auth-service-flow drawio](https://github.com/user-attachments/assets/0fe279b7-874c-404a-94bb-c5da16feec1c)

---

## Настройка ресурсного сервера
Для того чтобы твой сервер стал ресурсным в Random Walk API тебе нужно:
1. Добавить в проект библиотеку `implementation 'org.springframework.boot:spring-boot-starter-oauth2-resource-server'`
2. В property проекта добавить issuer_uri для ресурсного сервиса:
```
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://place-auth-service-issuer-uri-here
```
3. В свой securityConfig добавить поддержку ресурсного сервера и отключить поддержку сессий (так как авторизация через jwt токены), если они не нужны самому приложению:
```
                .sessionManagement(configurer ->
                        configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2ResourceServer(oauth2 ->
                       oauth2.jwt(jwt -> jwt
                               .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
```
4. Настроить конвертер jwt, для получения GrantedAuthority авторизированного пользователя в свой сервис, чтобы они использовались в @PreAuthorize аннотации
```
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(
                jwt -> {
                    List<String> list = jwt.getClaimAsStringList("authorities");
                    return list.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toSet());
                }
        );
        return converter;
    }
```
