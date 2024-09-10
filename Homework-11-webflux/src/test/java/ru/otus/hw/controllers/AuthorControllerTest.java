package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private AuthorMapper authorMapper;

    @Test
    public void testFindAll() {
        Author author1 = new Author("1", "Author One");
        Author author2 = new Author("2", "Author Two");
        AuthorDto authorDto1 = new AuthorDto("1", "Author One");
        AuthorDto authorDto2 = new AuthorDto("2", "Author Two");

        Mockito.when(authorService.findAll()).thenReturn(Flux.just(author1, author2));
        Mockito.when(authorMapper.toDto(author1)).thenReturn(authorDto1);
        Mockito.when(authorMapper.toDto(author2)).thenReturn(authorDto2);

        webTestClient.get().uri("/api/authors")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .contains(authorDto1, authorDto2);
    }

    @Test
    public void testFindById() {
        Author author = new Author("1", "Author One");
        AuthorDto authorDto = new AuthorDto("1", "Author One");

        Mockito.when(authorService.findById("1")).thenReturn(Mono.just(author));
        Mockito.when(authorMapper.toDto(author)).thenReturn(authorDto);

        webTestClient.get().uri("/api/authors/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthorDto.class)
                .isEqualTo(authorDto);
    }

    @Test
    public void testCreate() {
        Author author = new Author("1", "Author One");
        AuthorDto authorDto = new AuthorDto("1", "Author One");

        Mockito.when(authorService.save(any(Author.class))).thenReturn(Mono.just(author));
        Mockito.when(authorMapper.toEntity(authorDto)).thenReturn(author);
        Mockito.when(authorMapper.toDto(author)).thenReturn(authorDto);

        webTestClient.post().uri("/api/authors")
                .bodyValue(authorDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AuthorDto.class)
                .isEqualTo(authorDto);
    }

    @Test
    public void testUpdate() {
        Author author = new Author("1", "Author One Updated");
        AuthorDto authorDto = new AuthorDto("1", "Author One Updated");

        Mockito.when(authorService.save(any(Author.class))).thenReturn(Mono.just(author));
        Mockito.when(authorMapper.toEntity(authorDto)).thenReturn(author);
        Mockito.when(authorMapper.toDto(author)).thenReturn(authorDto);

        webTestClient.put().uri("/api/authors/1")
                .bodyValue(authorDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthorDto.class)
                .isEqualTo(authorDto);
    }

    @Test
    public void testDelete() {
        Mockito.when(authorService.deleteById("1")).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/authors/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}