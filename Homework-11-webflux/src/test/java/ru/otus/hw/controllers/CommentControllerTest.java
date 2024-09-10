package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.CommentService;

import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentMapper commentMapper;

    @Test
    public void testGetCommentById() {
        Comment comment = new Comment("1", "TestComment", null);
        CommentDto commentDto = new CommentDto("1", "TestComment", "Book1");

        Mockito.when(commentService.findCommentById("1")).thenReturn(Mono.just(comment));
        Mockito.when(commentMapper.toDto(comment)).thenReturn(commentDto);

        webTestClient.get().uri("/api/comments/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CommentDto.class)
                .isEqualTo(commentDto);
    }
    @Test
    public void testGetCommentsByBookId() {
        Comment comment1 = new Comment("1", "TestComment1", null);
        Comment comment2 = new Comment("2", "TestComment2", null);
        CommentDto commentDto1 = new CommentDto("1", "TestComment1", "Book1");
        CommentDto commentDto2 = new CommentDto("2", "TestComment2", "Book1");

        Mockito.when(commentService.findAllCommentsByBookId("Book1")).thenReturn(Flux.just(comment1, comment2));
        Mockito.when(commentMapper.toDto(comment1)).thenReturn(commentDto1);
        Mockito.when(commentMapper.toDto(comment2)).thenReturn(commentDto2);

        webTestClient.get().uri("/api/comments/by-book/Book1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CommentDto.class)
                .contains(commentDto1, commentDto2);
    }

    @Test
    public void testAddComment() {
        Comment comment = new Comment("1", "TestComment", null);
        CommentDto commentDto = new CommentDto("1", "TestComment", "Book1");

        Mockito.when(commentService.addComment(any(String.class), any(String.class))).thenReturn(Mono.just(comment));
        Mockito.when(commentMapper.toDto(comment)).thenReturn(commentDto);

        webTestClient.post().uri("/api/comments")
                .bodyValue(commentDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CommentDto.class)
                .isEqualTo(commentDto);
    }

    @Test
    public void testUpdateComment() {
        Comment comment = new Comment("1", "UpdatedComment", null);
        CommentDto commentDto = new CommentDto("1", "UpdatedComment", "Book1");

        Mockito.when(commentService.updateComment(any(String.class), any(String.class))).thenReturn(Mono.just(comment));
        Mockito.when(commentMapper.toDto(comment)).thenReturn(commentDto);

        webTestClient.put().uri("/api/comments/1")
                .bodyValue(commentDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CommentDto.class)
                .isEqualTo(commentDto);
    }

    @Test
    public void testDeleteComment() {
        Mockito.when(commentService.deleteCommentById("1")).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/comments/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}