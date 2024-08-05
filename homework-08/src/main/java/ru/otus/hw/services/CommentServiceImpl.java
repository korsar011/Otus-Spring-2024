package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Comment addComment(String bookId, String content) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));

        var comment = new Comment();
        comment.setContent(content);
        comment.setBook(book);

        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Comment> findCommentById(String commentId) {
        return commentRepository.findById(commentId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAllCommentsByBookId(String bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Transactional
    @Override
    public Comment updateComment(String commentId, String content) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setContent(content);
            return commentRepository.save(comment);
        } else {
            throw new EntityNotFoundException("Comment with id " + commentId + " not found");
        }
    }

    @Transactional
    @Override
    public void deleteCommentById(String commentId) {
        commentRepository.deleteById(commentId);
    }
}