-- Вставка данных в таблицу authors
INSERT INTO authors (full_name)
VALUES ('Author_1'), ('Author_2'), ('Author_3');

-- Вставка данных в таблицу genres
INSERT INTO genres (name)
VALUES ('Genre_1'), ('Genre_2'), ('Genre_3');

-- Вставка данных в таблицу books
INSERT INTO books (title, author_id, genre_id)
VALUES ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

-- Вставка данных в таблицу comments
INSERT INTO comments (content, book_id)
VALUES ('Comment_1', 1), ('Comment_2', 2), ('Comment_3', 3);