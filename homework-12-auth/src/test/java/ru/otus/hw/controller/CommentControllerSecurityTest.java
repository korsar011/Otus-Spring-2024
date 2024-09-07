package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.CustomUserDetailsService;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(CommentController.class)
@Import(SecurityConfiguration.class)
public class CommentControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    public void testAuthenticatedUserCanAccessCommentsByBookId() throws Exception {
        Comment comment = new Comment(1L, "This is a comment", new Book());
        given(commentService.findAllCommentsByBookId(1L)).willReturn(Collections.singletonList(comment));

        mockMvc.perform(get("/library/comments/by-book/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attribute("comments", Collections.singletonList(comment)));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    public void testAuthenticatedUserSeesMessageWhenNoCommentsForBook() throws Exception {
        given(commentService.findAllCommentsByBookId(1L)).willReturn(Collections.emptyList());

        mockMvc.perform(get("/library/comments/by-book/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "No comments found for the specified book."));
    }

    @Test
    public void testUnauthenticatedUserCannotAccessCommentsByBookId() throws Exception {
        mockMvc.perform(get("/library/comments/by-book/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }
}