package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.CommentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentMapper commentMapper;

    @Test
    public void testGetCommentById() throws Exception {
        Book book = new Book(1L, "Book Title", null, null);
        Comment comment = new Comment(1L, "Sample Comment", book);
        CommentDto commentDto = new CommentDto(1L, "Sample Comment", 1L);

        when(commentService.findCommentById(1L)).thenReturn(Optional.of(comment));
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        mockMvc.perform(get("/api/comments/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("Sample Comment"));
    }

    @Test
    public void testGetCommentById_NotFound() throws Exception {
        when(commentService.findCommentById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/comments/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetCommentsByBookId() throws Exception {
        Book book = new Book(1L, "Book Title", null, null);
        Comment comment = new Comment(1L, "Sample Comment", book);
        CommentDto commentDto = new CommentDto(1L, "Sample Comment", 1L);

        when(commentService.findAllCommentsByBookId(1L)).thenReturn(List.of(comment));
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        mockMvc.perform(get("/api/comments/by-book/{bookId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].content").value("Sample Comment"));
    }

    @Test
    public void testAddComment() throws Exception {
        Book book = new Book(1L, "Book Title", null, null);
        Comment comment = new Comment(null, "New Comment", book);
        CommentDto commentDto = new CommentDto(null, "New Comment", 1L);
        Comment savedComment = new Comment(1L, "New Comment", book);
        CommentDto savedCommentDto = new CommentDto(1L, "New Comment", 1L);

        when(commentMapper.toEntity(any(CommentDto.class))).thenReturn(comment);
        when(commentService.addComment(anyLong(), anyString())).thenReturn(savedComment);
        when(commentMapper.toDto(savedComment)).thenReturn(savedCommentDto);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"New Comment\",\"bookId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("New Comment"));
    }

    @Test
    public void testUpdateComment() throws Exception {
        Book book = new Book(1L, "Book Title", null, null);
        Comment existingComment = new Comment(1L, "Old Comment", book);
        Comment updatedComment = new Comment(1L, "Updated Comment", book);
        CommentDto commentDto = new CommentDto(1L, "Updated Comment", 1L);

        when(commentService.findCommentById(1L)).thenReturn(Optional.of(existingComment));
        when(commentService.updateComment(anyLong(), anyString())).thenReturn(updatedComment);
        when(commentMapper.toDto(updatedComment)).thenReturn(commentDto);
        when(commentMapper.toEntity(any(CommentDto.class))).thenReturn(existingComment);

        mockMvc.perform(put("/api/comments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Updated Comment\",\"bookId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("Updated Comment"));
    }

    @Test
    public void testDeleteComment() throws Exception {
        doNothing().when(commentService).deleteCommentById(1L);

        mockMvc.perform(delete("/api/comments/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(commentService, times(1)).deleteCommentById(1L);
    }
}
