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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    private final AuthorMapper authorMapper;

    @GetMapping
    public Flux<AuthorDto> findAll() {
        return authorService.findAll()
                .map(authorMapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<AuthorDto>> findById(@PathVariable String id) {
        return authorService.findById(id)
                .map(authorMapper::toDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AuthorDto> create(@RequestBody AuthorDto authorDto) {
        return authorService.save(authorMapper.toEntity(authorDto))
                .map(authorMapper::toDto);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<AuthorDto>> update(@PathVariable String id, @RequestBody AuthorDto authorDto) {
        Author authorToUpdate = authorMapper.toEntity(authorDto);
        authorToUpdate.setId(id);
        return authorService.save(authorToUpdate)
                .map(authorMapper::toDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return authorService.deleteById(id);
    }

}