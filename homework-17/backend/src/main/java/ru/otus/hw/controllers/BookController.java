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
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    private final BookMapper bookMapper;

    @GetMapping("/api/books")
    public List<BookDto> findAll() {
        return bookService.findAll().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/books/{id}")
    public ResponseEntity<BookDto> findById(@PathVariable long id) {
        return bookService.findById(id)
                .map(book -> ResponseEntity.ok(bookMapper.toDto(book)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/api/books")
    public ResponseEntity<BookDto> create(@RequestBody BookDto bookDto) {
        Book savedBook = bookService.insert(bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId());
        return ResponseEntity.status(HttpStatus.CREATED).body(bookMapper.toDto(savedBook));
    }

    @PutMapping("/api/books/{id}")
    public ResponseEntity<BookDto> update(@PathVariable long id, @RequestBody BookDto bookDto) {
        Book updatedBook = bookService.update(id, bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId());
        return ResponseEntity.ok(bookMapper.toDto(updatedBook));
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}