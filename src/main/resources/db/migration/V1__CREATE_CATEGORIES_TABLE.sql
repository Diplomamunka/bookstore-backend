CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

INSERT INTO categories (name)
VALUES
    ( 'romantic'),
    ( 'novel' ),
    ( 'crime' ),
    ( 'fantasy' ),
    ( 'humor' ),
    ( 'sci-fi' ),
    ( 'adventure' ),
    ( 'drama' ),
    ( 'fairy tale' ),
    ( 'juvenile' );