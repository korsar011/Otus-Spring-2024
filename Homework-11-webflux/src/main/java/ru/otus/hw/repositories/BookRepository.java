package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String>, CustomBookRepository {
    Mono<Book> findById(String id);

    Flux<Book> findByAuthorId(String authorId);

    Flux<Book> findAllByGenreId(String genreId);

    Mono<Long> countByAuthorId(String authorId);

    Mono<Long> countByGenreId(String genreId);

}
