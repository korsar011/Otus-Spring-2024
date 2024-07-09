package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations jdbc;

        public JdbcGenreRepository(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Genre> findAll() {
        return jdbc.query("select id, name from genres",
                new GnreRowMapper());
    }

    @Override
    public Optional<Genre> findById(long id) {
        Map<String, Object> params = Map.of("id", id);
        return Optional.ofNullable (jdbc.queryForObject(
                "select id, name from genres where id = :id",
                params, new GnreRowMapper()));
    }

    private static class GnreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
