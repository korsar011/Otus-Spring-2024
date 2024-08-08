package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(EmbeddedMongoConfig.class)
@DisplayName("Репозиторий для работы с книгами")
class MongoBookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @DisplayName("должен загружать информацию о нужной книге по её id")
    @Test
    void shouldFindExpectedBookById() {
        Author author = new Author("Author Name");
        Genre genre = new Genre("Genre Name");
        Book book = new Book("Test Book", author, genre);
        book = bookRepository.save(book);

        Book foundBook = bookRepository.findById(book.getId()).orElse(null);

        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getId()).isEqualTo(book.getId());
        assertThat(foundBook.getTitle()).isEqualTo("Test Book");
        assertThat(foundBook.getAuthor()).isEqualTo(author);
        assertThat(foundBook.getGenre()).isEqualTo(genre);
    }

    @DisplayName("должен загружать список всех книг с полной информацией о них")
    @Test
    void shouldReturnCorrectBooksListWithAllInfo() {
        Author author1 = new Author("Author One");
        Author author2 = new Author("Author Two");
        Genre genre1 = new Genre("Genre One");
        Genre genre2 = new Genre("Genre Two");

        Book book1 = new Book("Book One", author1, genre1);
        Book book2 = new Book("Book Two", author2, genre2);

        bookRepository.saveAll(List.of(book1, book2));

        List<Book> books = bookRepository.findAll();

        assertThat(books).isNotEmpty();
        assertThat(books).hasSizeGreaterThanOrEqualTo(2);
        assertThat(books).extracting(Book::getTitle)
                .containsExactlyInAnyOrder("Book One", "Book Two");
    }

    @DisplayName("должен корректно сохранять всю информацию о книге")
    @DirtiesContext
    @Test
    void shouldSaveAllBookInfo() {
        Author author = new Author("Test Author");
        Genre genre = new Genre("Test Genre");
        Book book = new Book("New Book", author, genre);

        bookRepository.save(book);

        assertThat(book.getId()).isNotNull();
        Book actualBook = bookRepository.findById(book.getId()).orElse(null);
        assertThat(actualBook).isNotNull();
        assertThat(actualBook.getTitle()).isEqualTo("New Book");
        assertThat(actualBook.getAuthor()).isEqualTo(author);
        assertThat(actualBook.getGenre()).isEqualTo(genre);
    }

    @DisplayName("должен удалять книгу по её id")
    @Test
    void shouldDeleteBookById() {
        Author author = new Author("Author Name");
        Genre genre = new Genre("Genre Name");
        Book book = new Book("Test Book", author, genre);
        book = bookRepository.save(book);

        bookRepository.deleteById(book.getId());

        Book deletedBook = bookRepository.findById(book.getId()).orElse(null);
        assertThat(deletedBook).isNull();
    }
}