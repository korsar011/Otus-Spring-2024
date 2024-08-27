package ru.otus.hw.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями")
@DataJpaTest
class JpaCommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать информацию о нужном комментарии по его id")
    @Test
    void shouldFindExpectedCommentById() {
        val optionalActualComment = commentRepository.findById(1L);
        val expectedComment = em.find(Comment.class, 1L);
        assertThat(optionalActualComment).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список всех комментариев для книги")
    @Test
    void shouldReturnCorrectCommentsListForBook() {
        val comments = commentRepository.findByBookId(1L);
        assertThat(comments).isNotNull().hasSize(1)
                .allMatch(c -> !c.getContent().isEmpty())
                .allMatch(c -> c.getBook() != null);
    }

    @DisplayName("должен корректно сохранять всю информацию о комментарии")
    @Test
    void shouldSaveAllCommentInfo() {
        val book = em.find(Book.class, 1L);
        val comment = new Comment(null, "New Comment", book);

        commentRepository.save(comment);
        assertThat(comment.getId()).isNotNull();

        val actualComment = em.find(Comment.class, comment.getId());
        assertThat(actualComment).isNotNull().matches(c -> !c.getContent().isEmpty())
                .matches(c -> c.getBook() != null);
    }

    @DisplayName("должен удалять комментарий по его id")
    @Test
    void shouldDeleteCommentById() {
        val comment = em.persistAndFlush(new Comment(null, "Comment to delete", em.find(Book.class, 1L)));
        commentRepository.deleteById(comment.getId());
        val deletedComment = em.find(Comment.class, comment.getId());
        assertThat(deletedComment).isNull();
    }
}