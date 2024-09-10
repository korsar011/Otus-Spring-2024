package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;

import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookMapper bookMapper;

    @Test
    public void testFindAll() {
        Book book1 = new Book("1", "Book One", null, null);
        Book book2 = new Book("2", "Book Two", null, null);
        BookDto bookDto1 = new BookDto("1", "Book One", "Author1", "Genre1");
        BookDto bookDto2 = new BookDto("2", "Book Two", "Author2", "Genre2");

        Mockito.when(bookService.findAll()).thenReturn(Flux.just(book1, book2));
        Mockito.when(bookMapper.toDto(book1)).thenReturn(bookDto1);
        Mockito.when(bookMapper.toDto(book2)).thenReturn(bookDto2);

        webTestClient.get().uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .contains(bookDto1, bookDto2);
    }

    @Test
    public void testFindById() {
        Book book = new Book("1", "Book One", null, null);
        BookDto bookDto = new BookDto("1", "Book One", "Author1", "Genre1");

        Mockito.when(bookService.findById("1")).thenReturn(Mono.just(book));
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        webTestClient.get().uri("/api/books/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(bookDto);
    }

    @Test
    public void testCreate() {
        Book book = new Book("1", "Book One", null, null);
        BookDto bookDto = new BookDto("1", "Book One", "Author1", "Genre1");

        Mockito.when(bookService.insert(any(), any(), any())).thenReturn(Mono.just(book));
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        webTestClient.post().uri("/api/books")
                .bodyValue(bookDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookDto.class)
                .isEqualTo(bookDto);
    }

    @Test
    public void testUpdate() {
        Book book = new Book("1", "Book One Updated", null, null);
        BookDto bookDto = new BookDto("1", "Book One Updated", "Author1", "Genre1");

        Mockito.when(bookService.update(any(), any(), any(), any())).thenReturn(Mono.just(book));
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        webTestClient.put().uri("/api/books/1")
                .bodyValue(bookDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(bookDto);
    }

    @Test
    public void testDelete() {
        Mockito.when(bookService.deleteById("1")).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/books/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}