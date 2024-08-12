package ru.otus.hw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.CommentService;

import java.util.List;

@Controller
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/library/comments/by-book/{bookId}")
    public String findAllCommentsByBookId(@PathVariable("bookId") long bookId, Model model) {
        List<Comment> comments = commentService.findAllCommentsByBookId(bookId);
        if (comments.isEmpty()) {
            model.addAttribute("message", "No comments found for the specified book.");
        } else {
            model.addAttribute("comments", comments);
        }
        return "comment_list";
    }
}