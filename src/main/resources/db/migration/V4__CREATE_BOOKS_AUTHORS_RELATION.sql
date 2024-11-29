CREATE TABLE IF NOT EXISTS books_authors (
    book_id BIGINT NOT NULL REFERENCES books(id),
    author_id BIGINT NOT NULL REFERENCES authors(id)
);