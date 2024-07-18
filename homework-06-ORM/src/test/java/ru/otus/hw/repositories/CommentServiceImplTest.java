package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.CommentService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest(properties = {"spring.shell.interactive.enabled=false"})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("должен корректно сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var comment = commentService.addComment(1L, "New Comment");
        assertThat(comment.getId()).isNotNull();
        assertThat(comment.getContent()).isEqualTo("New Comment");
        assertThat(comment.getBook().getId()).isEqualTo(1L);
        assertThatCode(() -> checkLazyInitialization(comment)).doesNotThrowAnyException();
    }

    @DisplayName("должен корректно обновлять комментарий")
    @Test
    void shouldUpdateComment() {
        var comment = commentService.updateComment(1L, "Updated Comment");
        assertThat(comment.getContent()).isEqualTo("Updated Comment");
        assertThatCode(() -> checkLazyInitialization(comment)).doesNotThrowAnyException();
    }

    @DisplayName("должен находить комментарий по id")
    @Test
    void shouldFindCommentById() {
        Optional<Comment> commentOptional = commentService.findCommentById(1L);
        assertThat(commentOptional).isPresent();
        Comment comment = commentOptional.get();
        assertThatCode(() -> checkLazyInitialization(comment)).doesNotThrowAnyException();
    }

    @DisplayName("должен удалять комментарий по id")
    @Test
    void shouldDeleteCommentById() {
        commentService.deleteCommentById(1L);
        Optional<Comment> comment = commentRepository.findById(1L);
        assertThat(comment).isEmpty();
    }

    @DisplayName("должен находить все комментарии по id книги")
    @Test
    void shouldFindAllCommentsByBookId() {
        List<Comment> comments = commentService.findAllCommentsByBookId(1L);
        assertThat(comments).isNotEmpty();
        comments.forEach(comment -> assertThatCode(() -> checkLazyInitialization(comment)).doesNotThrowAnyException());
    }

    @Transactional
    void checkLazyInitialization(Comment comment) {
        assertThat(comment.getBook().getTitle()).isNotNull();
    }
}