package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    private Long bookId;

    public Comment toDomainObject(Book book) {
        return new Comment(id, content, book);
    }

    public static CommentDto fromDomainObject(Comment comment) {
        return new CommentDto(comment.getId(), comment.getContent(), comment.getBook().getId());
    }
}