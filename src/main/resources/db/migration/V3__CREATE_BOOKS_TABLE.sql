CREATE TABLE IF NOT EXISTS books (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title text NOT NULL,
    price int NOT NULL,
    category VARCHAR(255) NOT NULL REFERENCES categories(name),
    short_description text,
    discount int CHECK(discount BETWEEN 0 AND 100) default 0,
    is_available boolean NOT NULL,
    release_date date,
    icon text
);