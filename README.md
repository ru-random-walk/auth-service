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

_**POST** /token_ - эндпоинт для получения токенов доступа до Random Walk API 

**Выходные данные** в формате _application/json_:
- access_token = jwt токен доступа
- refresh_token = токен для обновления access_token
- token_type = Тип access_token (Bearer)
- expires_in = Время до истечения токена в секундах

**Входные данные** различаются в зависимости от переданного grant_type.

#### Grant type urn:ietf:params:oauth:grant-type:token-exchange ####

Через данный grant_type осуществляется обмен токена из сторонней системы на токен для Random Walk API

**Принимает:**
- Credentials клиента в заголовке Authorization в формате Basic Auth
- Тело запроса в формате _application/x-www-form-urlencoded_:
 - grant_type (required) = urn:ietf:params:oauth:grant-type:token-exchange
 - subject_token (required) = Токен доступа из стороннего источника
 - subject_token_type (required) = Тип токена (На текущий момент поддерживается только Access Token)
 - subject_token_provider (required) = Название платформы в lowercase, которой пренадлежит предоставляемый токен доступа (google, yandex, etc.)

#### Grant type refresh_token ####

Через данный grant_type происходит обновление токена доступа через refresh_token

**Принимает:**
- Credentials клиента в заголовке Authorization в формате Basic Auth
- Тело запроса в формате _application/x-www-form-urlencoded_:
 - grant_type (required) = refresh_token
 - refresh_token (required) = значение полученного ранее refresh_token
