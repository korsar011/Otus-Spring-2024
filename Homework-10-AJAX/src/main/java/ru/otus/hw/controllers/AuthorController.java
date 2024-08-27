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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class AuthorController {
    private final AuthorService authorService;

    private final AuthorMapper authorMapper;

    @GetMapping("/api/authors")
    public List<AuthorDto> findAll() {
        return authorService.findAll().stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/authors/{id}")
    public ResponseEntity<AuthorDto> findById(@PathVariable long id) {
        return Optional.ofNullable(authorService.findById(id))
                .map(author -> ResponseEntity.ok(authorMapper.toDto(author)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/api/authors")
    public AuthorDto create(@RequestBody AuthorDto authorDto) {
        Author author = authorMapper.toEntity(authorDto);
        Author savedAuthor = authorService.save(author);
        return authorMapper.toDto(savedAuthor);
    }

    @PutMapping("/api/authors/{id}")
    public ResponseEntity<AuthorDto> update(@PathVariable long id, @RequestBody AuthorDto authorDto) {
        Author existingAuthor = authorService.findById(id);
        if (existingAuthor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        existingAuthor.setFullName(authorDto.getFullName());
        Author updatedAuthor = authorService.save(existingAuthor);
        return ResponseEntity.ok(authorMapper.toDto(updatedAuthor));
    }

    @DeleteMapping("/api/authors/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        authorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}