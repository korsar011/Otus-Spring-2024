package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Mono<Book> findById(String id);

    Mono<Long> countByAuthorId(String authorId);

    Mono<Long> countByGenreId(String authorId);
}
