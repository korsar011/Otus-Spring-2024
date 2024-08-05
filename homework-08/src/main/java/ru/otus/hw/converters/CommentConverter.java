package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;

@Component
public class CommentConverter {
    public String commentToString(Comment comment) {
        if (comment == null) {
            return "Comment not found";
        }
        String bookTitle = comment.getBook() != null ?
                comment.getBook().getTitle() : "Unknown book";
        return "Id: %s, Text: %s, Book: %s".formatted(
                comment.getId(),
                comment.getContent(),
                bookTitle);
    }
}