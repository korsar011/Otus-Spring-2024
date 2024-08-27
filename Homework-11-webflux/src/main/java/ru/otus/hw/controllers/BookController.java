package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.services.BookService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    private final BookMapper bookMapper;

    @GetMapping
    public Flux<BookDto> findAll() {
        return bookService.findAll()
                .map(bookMapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<BookDto>> findById(@PathVariable String id) {
        return bookService.findById(id)
                .map(bookMapper::toDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<BookDto>> create(@RequestBody BookDto bookDto) {
        return bookService.insert(bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId())
                .map(bookMapper::toDto)
                .map(bookDtoCreated -> ResponseEntity.status(HttpStatus.CREATED).body(bookDtoCreated));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<BookDto>> update(@PathVariable String id, @RequestBody BookDto bookDto) {
        return bookService.update(id, bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId())
                .map(bookMapper::toDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return bookService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
