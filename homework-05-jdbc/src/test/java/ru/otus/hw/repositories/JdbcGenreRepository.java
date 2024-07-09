package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами")
@JdbcTest
@Import(JdbcGenreRepository.class)

class JdbcGenreRepositoryTest {

    @Autowired
    private JdbcGenreRepository repository;

    @Test
    @DisplayName("должен загружать список всех жанров")
    void shouldReturnAllGenres() {
        List<Genre> genres = repository.findAll();
        assertThat(genres).isNotNull().hasSize(3);
    }

    @Test
    @DisplayName("должен загружать жанр по id")
    void shouldReturnGenreById() {
        long genreId = 1L;
        Genre genre = repository.findById(genreId).orElseThrow();
        assertThat(genre.getId()).isEqualTo(genreId);
    }
}