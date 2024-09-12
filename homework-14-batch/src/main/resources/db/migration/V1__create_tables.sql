-- Создание таблицы authors
CREATE TABLE authors (
                         id BIGSERIAL PRIMARY KEY,
                         full_name VARCHAR(255) NOT NULL
);

-- Создание таблицы genres
CREATE TABLE genres (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL
);

-- Создание таблицы books
CREATE TABLE books (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author_id BIGINT REFERENCES authors(id) ON DELETE CASCADE,
                       genre_id BIGINT REFERENCES genres(id) ON DELETE CASCADE
);

-- Создание таблицы comments
CREATE TABLE comments (
                          id BIGSERIAL PRIMARY KEY,
                          content VARCHAR(255) NOT NULL,
                          book_id BIGINT REFERENCES books(id) ON DELETE CASCADE
);