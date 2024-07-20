package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comment by id", key = "fc")
    public String findCommentById(long commentId) {
        return commentService.findCommentById(commentId)
                .map(commentConverter::commentToString)
                .orElse("Comment not found");
    }

    @ShellMethod(value = "Find all comments by book id", key = "fac")
    public String findAllCommentsByBookId(long bookId) {
        return commentService.findAllCommentsByBookId(bookId).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }
}