package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Comment addComment(String bookId, String content);

    Optional<Comment> findCommentById(String commentId);

    List<Comment> findAllCommentsByBookId(String bookId);

    Comment updateComment(String commentId, String content);

    void deleteCommentById(String commentId);
}