package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.CustomUserDetailsService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
@Import(SecurityConfiguration.class)
public class AuthorControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    public void testAuthenticatedUserCanAccessAuthorsPage() throws Exception {
        mockMvc.perform(get("/library/authors"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnauthenticatedUserCannotAccessAuthorsPage() throws Exception {
        mockMvc.perform(get("/library/authors"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    public void testAuthenticatedUserCanAccessNewAuthorForm() throws Exception {
        mockMvc.perform(get("/library/authors/new"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnauthenticatedUserCannotAccessNewAuthorForm() throws Exception {
        mockMvc.perform(get("/library/authors/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }

    @Test
    public void testUnauthenticatedUserCannotAccessEditAuthorForm() throws Exception {
        mockMvc.perform(get("/library/authors/edit/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    public void testAuthenticatedUserCanPostNewAuthor() throws Exception {
        mockMvc.perform(post("/library/authors")
                        .param("name", "New Author"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().equals("/library/authors"));
    }

    @Test
    public void testUnauthenticatedUserCannotPostNewAuthor() throws Exception {
        mockMvc.perform(post("/library/authors")
                        .param("name", "New Author"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    public void testAuthenticatedUserCanDeleteAuthor() throws Exception {
        mockMvc.perform(post("/library/authors/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().equals("/library/authors"));
    }

    @Test
    public void testUnauthenticatedUserCannotDeleteAuthor() throws Exception {
        mockMvc.perform(post("/library/authors/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }
}