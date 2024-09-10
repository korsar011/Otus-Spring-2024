package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    @Override
    public Mono<Comment> addComment(String bookId, String content) {
        return bookRepository.findById(bookId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id " + bookId + " not found")))
                .flatMap(book -> {
                    Comment comment = new Comment(content, book);
                    return commentRepository.save(comment);
                });
    }

    @Override
    public Mono<Comment> findCommentById(String commentId) {
        return commentRepository.findById(commentId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Comment with id " + commentId + " not found")));
    }

    @Override
    public Flux<Comment> findAllCommentsByBookId(String bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    public Mono<Comment> updateComment(String commentId, String content) {
        return commentRepository.findById(commentId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Comment with id " + commentId + " not found")))
                .flatMap(comment -> {
                    comment.setContent(content);
                    return commentRepository.save(comment);
                });
    }

    @Override
    public Mono<Void> deleteCommentById(String commentId) {
        return commentRepository.findById(commentId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Comment with id " + commentId + " not found")))
                .flatMap(comment -> commentRepository.deleteById(comment.getId()));
    }
}
