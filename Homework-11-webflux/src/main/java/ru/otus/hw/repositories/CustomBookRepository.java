package ru.otus.hw.repositories;

import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

public interface CustomBookRepository {
    Mono<Void> updateAuthorInBooks(String authorId, Author newAuthor);

    Mono<Void> updateManyByGenreId(String genreId, Genre newGenre);

}