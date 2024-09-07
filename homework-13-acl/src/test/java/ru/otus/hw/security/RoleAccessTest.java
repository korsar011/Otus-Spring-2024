package ru.otus.hw.security;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest
@Import(SecurityConfiguration.class)
@TestPropertySource(properties = {
        "spring.security.user.details-service.enabled=false"
})
public class RoleAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UserDetailsService userDetailsService;

    @ParameterizedTest
    @CsvSource({
            "admin, ADMIN, /library/authors, OK",
            "admin, ADMIN, /library/genres, OK",
            "admin, ADMIN, /library/books, OK",
            "user, USER, /library/books, OK",
            "user, USER, /library/authors, FORBIDDEN",
            "user, USER, /library/genres, FORBIDDEN"
    })
    public void testRoleBasedAccess(String username, String role, String url, String expectedStatus) throws Exception {
        if ("OK".equals(expectedStatus)) {
            mockMvc.perform(get(url)
                            .with(user(username).roles(role)))
                    .andExpect(status().isOk());
        } else if ("FORBIDDEN".equals(expectedStatus)) {
            mockMvc.perform(get(url)
                            .with(user(username).roles(role)))
                    .andExpect(status().isForbidden());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "admin, ADMIN, /library/genres, 1, Science Fiction, 3xxRedirection",
            "user, USER, /library/genres, 1, Science Fiction, FORBIDDEN"
    })
    public void testRoleBasedGenreAccess(String username, String role, String url, String id, String name, String expectedStatus) throws Exception {
        if ("3xxRedirection".equals(expectedStatus)) {
            mockMvc.perform(post(url)
                            .param("id", id)
                            .param("name", name)
                            .with(user(username).roles(role)))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/library/genres"));
        } else if ("FORBIDDEN".equals(expectedStatus)) {
            mockMvc.perform(post(url)
                            .param("id", id)
                            .param("name", name)
                            .with(user(username).roles(role)))
                    .andExpect(status().isForbidden());
        }
    }
}