CREATE TABLE if not exists ONE_TIME_PASSWORD(
    EMAIL varchar(255) primary key,
    PASSWORD varchar(255) not null,
    EXPIRES_AT timestamptz not null
);

ALTER TABLE if exists auth_user drop column if exists password;