-- Вставка пользователей
INSERT INTO users (username, password) VALUES
                                           ('admin', 'admin'),
                                           ('user1', 'user1'),
                                           ('user2', 'user2');

-- Вставка ролей
INSERT INTO roles (name) VALUES
                             ('ROLE_USER'),
                             ('ROLE_ADMIN');

-- Вставка связей пользователей и ролей
INSERT INTO user_roles (user_id, role_id) VALUES
                                              ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')),
                                              ((SELECT id FROM users WHERE username = 'user1'), (SELECT id FROM roles WHERE name = 'ROLE_USER')),
                                              ((SELECT id FROM users WHERE username = 'user2'), (SELECT id FROM roles WHERE name = 'ROLE_USER'));

-- Вставка авторов
INSERT INTO authors (full_name) VALUES
                                    ('Author_1'),
                                    ('Author_2'),
                                    ('Author_3');

-- Вставка жанров
INSERT INTO genres (name) VALUES
                              ('Genre_1'),
                              ('Genre_2'),
                              ('Genre_3');

-- Вставка книг
INSERT INTO books (title, author_id, genre_id) VALUES
                                                   ('BookTitle_1', 1, 1),
                                                   ('BookTitle_2', 2, 2),
                                                   ('BookTitle_3', 3, 3);

-- Вставка комментариев
INSERT INTO comments (content, book_id) VALUES
                                            ('Comment_1', 1),
                                            ('Comment_2', 2),
                                            ('Comment_3', 3);