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
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.services.GenreService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    private final GenreMapper genreMapper;

    @GetMapping
    public Flux<GenreDto> findAll() {
        return genreService.findAll()
                .map(genreMapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<GenreDto>> findById(@PathVariable String id) {
        return genreService.findById(id)
                .map(genreMapper::toDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GenreDto> create(@RequestBody GenreDto genreDto) {
        return genreService.save(genreMapper.toEntity(genreDto))
                .map(genreMapper::toDto);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<GenreDto>> update(@PathVariable String id, @RequestBody GenreDto genreDto) {
        return genreService.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Genre not found")))
                .flatMap(genre -> {
                    genre.setName(genreDto.getName());
                    return genreService.save(genre);
                })
                .map(genreMapper::toDto)
                .map(ResponseEntity::ok)
                .onErrorResume(EntityNotFoundException.class,
                        e -> Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return genreService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(EntityNotFoundException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }
}
