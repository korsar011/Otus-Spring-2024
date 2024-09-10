package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exeptions.GenreHasBooksException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    public Flux<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Mono<Genre> findById(String id) {
        return genreRepository.findById(id);
    }

    @Override
    public Mono<Genre> save(Genre genre) {
        return genreRepository.save(genre)
                .flatMap(savedGenre -> {
                    if (genre.getId() != null) {
                        return bookRepository.updateManyByGenreId(genre.getId(), savedGenre)
                                .then(Mono.just(savedGenre));
                    }
                    return Mono.just(savedGenre);
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return bookRepository.countByGenreId(id)
                .flatMap(bookCount -> {
                    if (bookCount > 0) {
                        return Mono.error(new GenreHasBooksException(
                                "Cannot delete genre with associated books, kindly delete books first."));
                    }
                    return genreRepository.deleteById(id);
                });
    }
}
