package ru.otus.hw.services;

import ru.otus.hw.models.Author;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthorService {
    Flux<Author> findAll();

    Mono<Author> findById(String id);

    Mono<Author> save(Author author);

    Mono<Void> deleteById(String id);
}