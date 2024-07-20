package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Override
    public void deleteById(Long id) {
        Comment comment = em.find(Comment.class, id);
        if (comment != null) {
            em.remove(comment);
        }
    }

    @Override
    public Optional<Comment> findById(Long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("comment-book-entity-graph");
        Map<String, Object> hints = new HashMap<>();
        hints.put("jakarta.persistence.fetchgraph", entityGraph);

        Comment comment = em.find(Comment.class, id, hints);
        return Optional.ofNullable(comment);
    }

    @Override
    public List<Comment> findByBookId(Long bookId) {
        Book book = em.find(Book.class, bookId);

        if (book == null) {
            return Collections.emptyList();
        }

        return em.createQuery("SELECT c FROM Comment c WHERE c.book.id = :bookId", Comment.class)
                .setParameter("bookId", bookId)
                .getResultList();
    }
}