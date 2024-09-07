-- Таблица пользователей
CREATE TABLE users (
                       id bigserial PRIMARY KEY,
                       username varchar(255) NOT NULL UNIQUE,
                       password varchar(255) NOT NULL
);

-- Таблица ролей
CREATE TABLE roles (
                       id bigserial PRIMARY KEY,
                       name varchar(255) NOT NULL UNIQUE
);

-- Таблица связей пользователей и ролей
CREATE TABLE user_roles (
                            user_id bigint REFERENCES users(id) ON DELETE CASCADE,
                            role_id bigint REFERENCES roles(id) ON DELETE CASCADE,
                            PRIMARY KEY (user_id, role_id)
);

-- Таблица авторов
CREATE TABLE authors (
                         id bigserial PRIMARY KEY,
                         full_name varchar(255) NOT NULL
);

-- Таблица жанров
CREATE TABLE genres (
                        id bigserial PRIMARY KEY,
                        name varchar(255) NOT NULL
);

-- Таблица книг
CREATE TABLE books (
                       id bigserial PRIMARY KEY,
                       title varchar(255) NOT NULL,
                       author_id bigint REFERENCES authors(id) ON DELETE CASCADE,
                       genre_id bigint REFERENCES genres(id) ON DELETE CASCADE
);

-- Таблица комментариев
CREATE TABLE comments (
                          id bigserial PRIMARY KEY,
                          content varchar(255) NOT NULL,
                          book_id bigint REFERENCES books(id) ON DELETE CASCADE
);