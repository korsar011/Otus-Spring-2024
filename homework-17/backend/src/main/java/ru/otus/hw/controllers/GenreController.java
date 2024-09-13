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
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class GenreController {

    private final GenreService genreService;

    private final GenreMapper genreMapper;

    @GetMapping("/api/genres")
    public List<GenreDto> findAll() {
        return genreService.findAll().stream()
                .map(genreMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/genres/{id}")
    public ResponseEntity<GenreDto> findById(@PathVariable long id) {
        return genreService.findById(id)
                .map(genre -> ResponseEntity.ok(genreMapper.toDto(genre)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/api/genres")
    public ResponseEntity<GenreDto> create(@RequestBody GenreDto genreDto) {
        Genre genre = genreMapper.toEntity(genreDto);
        genreService.save(genre);
        return ResponseEntity.status(HttpStatus.CREATED).body(genreMapper.toDto(genre));
    }

    @PutMapping("/api/genres/{id}")
    public ResponseEntity<GenreDto> update(@PathVariable long id, @RequestBody GenreDto genreDto) {
        Genre genre = genreService.findById(id).orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        genre.setName(genreDto.getName());
        genreService.save(genre);
        return ResponseEntity.ok(genreMapper.toDto(genre));
    }

    @DeleteMapping("/api/genres/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        genreService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}