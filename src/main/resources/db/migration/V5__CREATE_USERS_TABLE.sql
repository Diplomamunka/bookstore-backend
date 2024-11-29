CREATE TABLE IF NOT EXISTS users (
    login TEXT PRIMARY KEY,
    password TEXT,
    user_role VARCHAR(25) NOT NULL,
    last_name TEXT NOT NULL,
    first_name TEXT NOT NULL
);