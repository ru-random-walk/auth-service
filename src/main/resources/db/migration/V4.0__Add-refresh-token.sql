CREATE TABLE IF NOT EXISTS REFRESH_TOKEN(
    token uuid primary key,
    user_id uuid unique references auth_user,
    expires_at timestamp not null
);
