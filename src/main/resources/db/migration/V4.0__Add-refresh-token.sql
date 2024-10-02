CREATE TABLE IF NOT EXISTS REFRESH_TOKEN(
    user_id uuid primary key references auth_user,
    token uuid not null,
    expires_at timestamp not null
);
