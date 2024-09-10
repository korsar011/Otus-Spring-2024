package ru.otus.hw.migration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

@Configuration
public class MigrationConfig {

    @Bean
    public CommandLineRunner migrateData(
            AuthorRepository authorRepository,
            BookRepository bookRepository,
            GenreRepository genreRepository,
            CommentRepository commentRepository) {
        return args -> {
            Genre fiction = new Genre("Fiction");
            Genre nonFiction = new Genre("Non-Fiction");

            Author author1 = new Author("Author One");
            Author author2 = new Author("Author Two");

            Book book1 = new Book("Book One", author1, fiction);
            Book book2 = new Book("Book Two", author2, nonFiction);

            Mono<Void> clearGenres = genreRepository.deleteAll().then();
            Mono<Void> clearAuthors = authorRepository.deleteAll().then();
            Mono<Void> clearBooks = bookRepository.deleteAll().then();
            Mono<Void> clearComments = commentRepository.deleteAll().then();

            Mono<Void> migrateData = clearGenres
                    .then(clearAuthors).then(clearBooks)
                    .then(clearComments).thenMany(Flux.just(fiction, nonFiction).flatMap(genreRepository::save))
                    .thenMany(Flux.just(author1, author2).flatMap(authorRepository::save))
                    .thenMany(Flux.just(book1, book2).flatMap(bookRepository::save))
                    .then(Mono.fromRunnable(() -> System.out.println("Migration completed")));
            migrateData.subscribe();
        };
    }
}
