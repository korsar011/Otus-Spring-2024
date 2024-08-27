package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    @Override
    public Mono<Book> findById(String id) {
        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id " + id + " not found")));
    }

    @Override
    public Flux<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Mono<Book> insert(String title, String authorId, String genreId) {
        return authorRepository.findById(authorId)
                .flatMap(author -> genreRepository.findById(genreId)
                        .map(genre -> new Book(title, author, genre))
                        .flatMap(bookRepository::save));
    }

    @Override
    public Mono<Book> update(String id, String title, String authorId, String genreId) {
        return bookRepository.findById(id)
                .flatMap(book -> authorRepository.findById(authorId)
                        .flatMap(author -> genreRepository.findById(genreId)
                                .map(genre -> {
                                    book.setTitle(title);
                                    book.setAuthor(author);
                                    book.setGenre(genre);
                                    return book;
                                })
                                .flatMap(bookRepository::save)));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return commentRepository.deleteByBookId(id)
                .then(bookRepository.deleteById(id));
    }
}
