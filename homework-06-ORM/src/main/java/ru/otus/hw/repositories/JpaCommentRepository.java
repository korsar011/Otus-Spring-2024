package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

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
        Comment comment = em.createQuery("SELECT c FROM Comment c WHERE c.id = :id", Comment.class)
                .setParameter("id", id)
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null); // Если комментарий не найден, вернем null

        return Optional.ofNullable(comment);
    }

    @Override
    public List<Comment> findByBookId(Long bookId) {
        EntityGraph<?> entityGraph = em.getEntityGraph("comment-book-entity-graph");
        return em.createQuery("SELECT c FROM Comment c WHERE c.book.id = :bookId", Comment.class)
                .setParameter("bookId", bookId)
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }
}