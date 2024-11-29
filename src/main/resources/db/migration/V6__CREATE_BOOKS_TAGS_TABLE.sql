CREATE TABLE IF NOT EXISTS books_tags (
    book_id BIGINT NOT NULL REFERENCES books(id),
    tag VARCHAR(45)
);