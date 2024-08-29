package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @Override
    public Flux<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Mono<Author> findById(String id) {
        return authorRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Author not found")));
    }

    @Override
    public Mono<Author> save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return bookRepository.countByAuthorId(id)
                .flatMap(bookCount -> {
                    if (bookCount > 0) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,
                                "Cannot delete author with associated books, kindly delete book first."));
                    }
                    return authorRepository.deleteById(id);
                });
    }
}