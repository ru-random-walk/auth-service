CREATE TYPE AUTH_TYPE AS ENUM ('PASSWORD', 'GOOGLE', 'YANDEX');
CREATE CAST (character varying as AUTH_TYPE) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS AUTH_USER (
    ID UUID PRIMARY KEY,
    FULL_NAME varchar(256),
    USERNAME varchar(256) UNIQUE NOT NULL,
    EMAIL varchar(256) UNIQUE NOT NULL,
    PASSWORD varchar(256),
    ENABLED BOOLEAN DEFAULT true,
    ACCOUNT_TYPE AUTH_TYPE
);

CREATE TABLE IF NOT EXISTS ROLE (
    ID INT PRIMARY KEY,
    NAME varchar(50)
);

CREATE TABLE IF NOT EXISTS USER_ROLE (
    USER_ID UUID REFERENCES AUTH_USER,
    ROLE_ID INT REFERENCES ROLE
);

CREATE TABLE IF NOT EXISTS OAUTH_CLIENT (
    CLIENT_ID varchar(100) PRIMARY KEY,
    CLIENT_SECRET varchar(256)
);

CREATE TABLE IF NOT EXISTS OAUTH_SCOPE (
    ID INT PRIMARY KEY,
    NAME varchar(50)
);

CREATE TABLE IF NOT EXISTS CLIENT_SCOPE (
    CLIENT_ID varchar(100) REFERENCES OAUTH_CLIENT,
    SCOPE_ID INT REFERENCES OAUTH_SCOPE
);
