package ru.otus.hw.testChangelogs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.config.EmbeddedMongoConfig;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(EmbeddedMongoConfig.class)
class TestDatabaseChangeLogTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @DisplayName("должен корректно применять тестовый ChangeLog")
    @DirtiesContext
    @Test
    void shouldApplyTestChangeLog() {
        List<Genre> genres = genreRepository.findAll();
        List<Author> authors = authorRepository.findAll();
        List<Book> books = bookRepository.findAll();

        assertThat(genres).hasSize(2)
                .extracting(Genre::getName)
                .containsExactlyInAnyOrder("Mystery", "Science Fiction");

        assertThat(authors).hasSize(3)
                .extracting(Author::getFullName)
                .containsExactlyInAnyOrder("Test Author One", "Test Author Two", "Test Author Three");

        assertThat(books).hasSize(3)
                .extracting(Book::getTitle)
                .containsExactlyInAnyOrder("Test Book One", "Test Book Two", "Test Book Three");
    }
}