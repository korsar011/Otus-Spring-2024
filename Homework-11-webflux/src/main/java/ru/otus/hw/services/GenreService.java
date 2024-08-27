package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Genre;

public interface GenreService {
    Flux<Genre> findAll();

    Mono<Genre> findById(String id);

    Mono<Genre> save(Genre genre);

    Mono<Void> deleteById(String id);
}
