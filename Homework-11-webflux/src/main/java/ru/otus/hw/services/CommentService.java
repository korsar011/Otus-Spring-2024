package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Comment;

public interface CommentService {
    public Mono<Comment> addComment(String bookId, String content)        ;

    public Mono<Comment> findCommentById(String commentId);

    public Flux<Comment> findAllCommentsByBookId(String bookId);

    public Mono<Comment> updateComment(String commentId, String content);

    public Mono<Void> deleteCommentById(String commentId);
}