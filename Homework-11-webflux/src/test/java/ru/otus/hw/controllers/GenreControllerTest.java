package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(GenreController.class)
public class GenreControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenreService genreService;

    @MockBean
    private GenreMapper genreMapper;

    @Test
    public void testFindAll() {
        Genre genre1 = new Genre("1", "Fiction");
        Genre genre2 = new Genre("2", "Non-Fiction");
        GenreDto genreDto1 = new GenreDto("1", "Fiction");
        GenreDto genreDto2 = new GenreDto("2", "Non-Fiction");

        Mockito.when(genreService.findAll()).thenReturn(Flux.just(genre1, genre2));
        Mockito.when(genreMapper.toDto(genre1)).thenReturn(genreDto1);
        Mockito.when(genreMapper.toDto(genre2)).thenReturn(genreDto2);

        webTestClient.get().uri("/api/genres")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .contains(genreDto1, genreDto2);
    }

    @Test
    public void testFindById() {
        Genre genre = new Genre("1", "Fiction");
        GenreDto genreDto = new GenreDto("1", "Fiction");

        Mockito.when(genreService.findById("1")).thenReturn(Mono.just(genre));
        Mockito.when(genreMapper.toDto(genre)).thenReturn(genreDto);

        webTestClient.get().uri("/api/genres/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(GenreDto.class)
                .isEqualTo(genreDto);
    }

    @Test
    public void testCreate() {
        Genre genre = new Genre("1", "Fiction");
        GenreDto genreDto = new GenreDto("1", "Fiction");

        Mockito.when(genreService.save(any(Genre.class))).thenReturn(Mono.just(genre));
        Mockito.when(genreMapper.toEntity(genreDto)).thenReturn(genre);
        Mockito.when(genreMapper.toDto(genre)).thenReturn(genreDto);

        webTestClient.post().uri("/api/genres")
                .bodyValue(genreDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(GenreDto.class)
                .isEqualTo(genreDto);
    }

    @Test
    public void testUpdate() {
        Genre genre = new Genre("1", "Fiction Updated");
        GenreDto genreDto = new GenreDto("1", "Fiction Updated");

        Mockito.when(genreService.findById("1")).thenReturn(Mono.just(genre));
        Mockito.when(genreService.save(any(Genre.class))).thenReturn(Mono.just(genre));
        Mockito.when(genreMapper.toEntity(genreDto)).thenReturn(genre);
        Mockito.when(genreMapper.toDto(genre)).thenReturn(genreDto);

        webTestClient.put().uri("/api/genres/1")
                .bodyValue(genreDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GenreDto.class)
                .isEqualTo(genreDto);
    }

    @Test
    public void testDelete() {
        Mockito.when(genreService.deleteById("1")).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/genres/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}