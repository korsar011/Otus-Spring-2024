package ru.otus.hw.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.CommentService;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(CommentController.class)
@Import(SecurityConfiguration.class)
@TestPropertySource(properties = {
        "spring.security.user.details-service.enabled=false"
})
public class CommentControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UserDetailsService userDetailsService;

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @ParameterizedTest
    @CsvSource({
            "1, This is a comment, true, No comments found for the specified book.",
            "1, , false, No comments found for the specified book."
    })
    public void testAuthenticatedUserCanAccessCommentsByBookId(Long bookId, String commentText, boolean hasComments, String emptyMessage) throws Exception {
        if (hasComments) {
            Comment comment = new Comment(1L, commentText, new Book());
            given(commentService.findAllCommentsByBookId(bookId)).willReturn(Collections.singletonList(comment));
            mockMvc.perform(get("/library/comments/by-book/{bookId}", bookId))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("comments"))
                    .andExpect(model().attribute("comments", Collections.singletonList(comment)));
        } else {
            given(commentService.findAllCommentsByBookId(bookId)).willReturn(Collections.emptyList());
            mockMvc.perform(get("/library/comments/by-book/{bookId}", bookId))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("message"))
                    .andExpect(model().attribute("message", emptyMessage));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "2"
    })
    public void testUnauthenticatedUserCannotAccessCommentsByBookId(Long bookId) throws Exception {
        mockMvc.perform(get("/library/comments/by-book/{bookId}", bookId))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }
}