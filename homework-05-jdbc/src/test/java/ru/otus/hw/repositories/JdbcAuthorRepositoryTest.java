package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с авторами")
@JdbcTest
@Import(JdbcAuthorRepository.class)
class JdbcAuthorRepositoryTest {

    @Autowired
    private JdbcAuthorRepository repository;

    @Test
    @DisplayName("должен загружать список всех авторов")
    void shouldReturnAllAuthors() {
        List<Author> authors = repository.findAll();
        assertThat(authors).isNotNull().hasSize(3);
    }

    @Test
    @DisplayName("должен загружать автора по id")
    void shouldReturnAuthorById() {
        long authorId = 1L;
        Author author = repository.findById(authorId).orElseThrow();
        assertThat(author.getId()).isEqualTo(authorId);
    }
}