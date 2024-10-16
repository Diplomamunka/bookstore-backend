CREATE TABLE IF NOT EXISTS book_tags (
    book_id BIGINT NOT NULL REFERENCES books(id),
    tag varchar(45)
);