package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.book.id = :bookId")
    List<Comment> findByBookId(@Param("bookId") Long bookId);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.book.id = :bookId")
    void deleteByBookId(@Param("bookId") Long bookId);
}