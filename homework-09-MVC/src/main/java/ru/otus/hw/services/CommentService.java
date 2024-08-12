package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Comment addComment(long bookId, String text);

    Optional<Comment> findCommentById(long commentId);

    List<Comment> findAllCommentsByBookId(long bookId);

    Comment updateComment(long commentId, String text);

    void deleteCommentById(long commentId);
}