package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.config.EmbeddedMongoConfig;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({CommentServiceImpl.class, EmbeddedMongoConfig.class})
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book("Test Book", new Author("Author"), new Genre("Genre"));
        testBook = bookRepository.save(testBook);
    }

    @DisplayName("должен корректно сохранять новый комментарий")
    @Test
    @DirtiesContext
    void shouldSaveNewComment() {
        var comment = commentService.addComment(testBook.getId(), "New Comment");
        assertThat(comment.getId()).isNotNull();
        assertThat(comment.getContent()).isEqualTo("New Comment");
        assertThat(comment.getBook().getId()).isEqualTo(testBook.getId());
    }

    @DisplayName("должен корректно обновлять комментарий")
    @Test
    @DirtiesContext
    void shouldUpdateComment() {
        var comment = commentService.addComment(testBook.getId(), "Initial Comment");
        var updatedComment = commentService.updateComment(comment.getId(), "Updated Comment");
        assertThat(updatedComment.getContent()).isEqualTo("Updated Comment");
    }

    @DisplayName("должен находить комментарий по id")
    @Test
    void shouldFindCommentById() {
        var comment = commentService.addComment(testBook.getId(), "New Comment");
        Optional<Comment> commentOptional = commentService.findCommentById(comment.getId());
        assertThat(commentOptional).isPresent();
        Comment foundComment = commentOptional.get();
        assertThat(foundComment.getContent()).isEqualTo("New Comment");
    }

    @DisplayName("должен удалять комментарий по id")
    @Test
    void shouldDeleteCommentById() {
        var comment = commentService.addComment(testBook.getId(), "To be deleted");
        commentService.deleteCommentById(comment.getId());
        Optional<Comment> commentOptional = commentRepository.findById(comment.getId());
        assertThat(commentOptional).isEmpty();
    }

    @DisplayName("должен находить все комментарии по id книги")
    @Test
    void shouldFindAllCommentsByBookId() {
        commentService.addComment(testBook.getId(), "Comment 1");
        commentService.addComment(testBook.getId(), "Comment 2");
        List<Comment> comments = commentService.findAllCommentsByBookId(testBook.getId());
        assertThat(comments).hasSize(2);
    }
}