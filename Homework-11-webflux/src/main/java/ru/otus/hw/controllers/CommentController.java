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
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.services.CommentService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CommentDto>> getCommentById(@PathVariable("id") String id) {
        return commentService.findCommentById(id)
                .map(comment -> ResponseEntity.ok(commentMapper.toDto(comment)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-book/{bookId}")
    public Flux<CommentDto> getCommentsByBookId(@PathVariable("bookId") String bookId) {
        return commentService.findAllCommentsByBookId(bookId)
                .map(commentMapper::toDto);
    }

    @PostMapping
    public Mono<ResponseEntity<CommentDto>> addComment(@RequestBody CommentDto commentDto) {
        return commentService.addComment(commentDto.getBookId(), commentDto.getContent())
                .map(comment -> ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.toDto(comment)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CommentDto>> updateComment(@PathVariable("id") String id,
                                                          @RequestBody CommentDto commentDto) {
        return commentService.updateComment(id, commentDto.getContent())
                .map(commentMapper::toDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteComment(@PathVariable("id") String id) {
        return commentService.deleteCommentById(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(EntityNotFoundException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }
}
