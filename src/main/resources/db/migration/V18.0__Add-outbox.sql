CREATE TABLE if not exists OUTBOX(
    id uuid primary key,
    payload text,
    topic varchar,
    created_at timestamptz,
    sent bool default false
);