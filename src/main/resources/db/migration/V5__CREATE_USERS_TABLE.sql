CREATE TABLE IF NOT EXISTS USERS (
    login text PRIMARY KEY,
    password text NOT NULL,
    user_role varchar(25),
    last_name text,
    first_name text
);

INSERT INTO USERS VALUES ('adam.admin@bookstore.com', 'xxxx', 'ADMIN', 'Admin', 'Adam');