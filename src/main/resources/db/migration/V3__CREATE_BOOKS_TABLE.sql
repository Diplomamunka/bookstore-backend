CREATE TABLE IF NOT EXISTS books (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title TEXT NOT NULL,
    price INT NOT NULL,
    category VARCHAR(255) NOT NULL REFERENCES categories(name),
    short_description TEXT,
    discount INT CHECK(discount BETWEEN 0 AND 100) default 0,
    is_available BOOLEAN NOT NULL,
    release_date DATE,
    icon TEXT
);