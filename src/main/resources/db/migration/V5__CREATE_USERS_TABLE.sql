CREATE TABLE IF NOT EXISTS USERS (
    login text PRIMARY KEY,
    password text,
    user_role varchar(25) NOT NULL,
    last_name text NOT NULL,
    first_name text NOT NULL
);