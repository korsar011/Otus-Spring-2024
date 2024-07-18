package ru.otus.hw.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbc;

    public JdbcBookRepository(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Book> findById(long id) {
        String selectById = "SELECT b.id as book_id, b.title as title, " +
                "a.id as author_id, a.full_name as full_name, " +
                "g.id as genre_id, g.name as name " +
                "FROM books b " +
                "INNER JOIN authors a on b.author_id = a.id " +
                "LEFT JOIN genres g on b.genre_id = g.id " +
                "WHERE b.id = :id";
        try {
            return Optional.ofNullable(jdbc.queryForObject(selectById, Map.of("id", id), new BookRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        String selectById = "SELECT b.id as book_id, b.title as title, " +
                "a.id as author_id, a.full_name as full_name, " +
                "g.id as genre_id, g.name as name " +
                "FROM books b " +
                "JOIN authors a on b.author_id = a.id " +
                "LEFT JOIN genres g on b.genre_id = g.id ";
        return jdbc.query(selectById, new BookRowMapper());
        }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        String deleteById = "DELETE FROM books WHERE id = :id";
        jdbc.update(deleteById, Map.of("id", id));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        String insertSql = "INSERT INTO books (title, author_id, genre_id) VALUES (:title, :author_id, :genre_id)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());

        jdbc.update(insertSql, params, keyHolder);

        //noinspection DataFlowIssue
        long newId = keyHolder.getKey().longValue();
        book.setId(newId);
        return book;
    }

    private Book update(Book book) {
        String updateSql = "UPDATE books SET title = :title," +
                " author_id = :author_id, genre_id = :genre_id WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", book.getId());
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());


        int updateCount = jdbc.update(updateSql, params);
        if (updateCount == 0) {
            throw new EntityNotFoundException("Book with id " + book.getId() + " not found");
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                Author author = new Author(rs.getLong("author_id"), rs.getString("full_name"));
                Genre genre = new Genre (rs.getLong("genre_id"), rs.getString("name"));
                return new Book(id, title, author, genre);
            }
        }
    }

