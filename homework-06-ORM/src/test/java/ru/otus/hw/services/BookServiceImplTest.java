package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@SpringBootTest(properties = {"spring.shell.interactive.enabled=false"})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("должен корректно сохранять новую книгу")
    @Test
    @DirtiesContext
    void shouldSaveNewBook() {
        var book = bookService.insert("New Book", 1L, 1L);
        assertThat(book.getId()).isNotNull();
        assertThat(book.getTitle()).isEqualTo("New Book");
        assertThat(book.getAuthor().getId()).isEqualTo(1L);
        assertThat(book.getGenre().getId()).isEqualTo(1L);
        assertThatCode(() -> checkLazyInitialization(book)).doesNotThrowAnyException();
    }

    @DisplayName("должен корректно обновлять книгу")
    @Test
    @DirtiesContext
    void shouldUpdateBook() {
        var book = bookService.update(1L, "Updated Title", 1L, 1L);
        assertThat(book.getTitle()).isEqualTo("Updated Title");
        assertThatCode(() -> checkLazyInitialization(book)).doesNotThrowAnyException();
    }

    @DisplayName("должен находить книгу по id")
    @Test
    void shouldFindBookById() {
        Optional<Book> bookOptional = bookService.findById(1L);
        assertThat(bookOptional).isPresent();
        Book book = bookOptional.get();

        assertThatCode(() -> checkLazyInitialization(book)).doesNotThrowAnyException();
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    @DirtiesContext
    void shouldDeleteBookById() {
        bookService.deleteById(1L);
        Optional<Book> book = bookRepository.findById(1L);
        assertThat(book).isEmpty();
    }

    @DisplayName("должен находить все книги")
    @Test
    void shouldFindAllBooks() {
        List<Book> books = bookService.findAll();
        assertThat(books).isNotEmpty();
        books.forEach(book -> assertThatCode(() -> checkLazyInitialization(book)).doesNotThrowAnyException());
    }

    void checkLazyInitialization(Book book) {
        assertThat(book.getAuthor().getFullName()).isNotNull();
        assertThat(book.getGenre().getName()).isNotNull();
    }
}