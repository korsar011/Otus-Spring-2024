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
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @GetMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("id") long id) {
        return commentService.findCommentById(id)
                .map(comment -> ResponseEntity.ok(commentMapper.toDto(comment)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/api/comments/by-book/{bookId}")
    public ResponseEntity<List<CommentDto>> getCommentsByBookId(@PathVariable("bookId") long bookId) {
        List<Comment> comments = commentService.findAllCommentsByBookId(bookId);
        if (comments.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            List<CommentDto> commentDtos = comments.stream()
                    .map(commentMapper::toDto)
                    .toList();
            return ResponseEntity.ok(commentDtos);
        }
    }

    @PostMapping("/api/comments")
    public ResponseEntity<CommentDto> addComment(@RequestBody CommentDto commentDto) {
        try {
            Comment comment = commentMapper.toEntity(commentDto);
            Comment createdComment = commentService.addComment(comment.getBook().getId(), comment.getContent());
            return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.toDto(createdComment));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("id") long id,
                                                    @RequestBody CommentDto commentDto) {
        try {
            Comment comment = commentMapper.toEntity(commentDto);

            if (comment == null || comment.getContent() == null) {
                return ResponseEntity.badRequest().build();
            }

            Comment updatedComment = commentService.updateComment(id, comment.getContent());

            CommentDto updatedCommentDto = commentMapper.toDto(updatedComment);
            return ResponseEntity.ok(updatedCommentDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") long id) {
        try {
            commentService.deleteCommentById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}