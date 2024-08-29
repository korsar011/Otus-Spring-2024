package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Genre;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.CustomUserDetailsService;
import ru.otus.hw.services.GenreService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenreController.class)
@Import(SecurityConfiguration.class)
public class GenreControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    public void testAuthenticatedUserCanAccessAllGenres() throws Exception {
        Genre genre = new Genre(1L, "Science Fiction");
        given(genreService.findAll()).willReturn(Collections.singletonList(genre));

        mockMvc.perform(get("/library/genres"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", Collections.singletonList(genre)));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    public void testAuthenticatedUserCanAccessNewGenreForm() throws Exception {
        mockMvc.perform(get("/library/genres/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("genre"))
                .andExpect(model().attribute("genre", new Genre()));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    public void testAuthenticatedUserCanSaveGenre() throws Exception {
        Genre genre = new Genre(1L, "Science Fiction");

        mockMvc.perform(post("/library/genres")
                        .param("id", "1")
                        .param("name", "Science Fiction"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/library/genres"));

        then(genreService).should().save(genre);
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    public void testAuthenticatedUserCanAccessEditGenreForm() throws Exception {
        Genre genre = new Genre(1L, "Science Fiction");
        given(genreService.findById(1L)).willReturn(Optional.of(genre));

        mockMvc.perform(get("/library/genres/edit/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("genre"))
                .andExpect(model().attribute("genre", genre));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    public void testAuthenticatedUserCanDeleteGenre() throws Exception {
        mockMvc.perform(post("/library/genres/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().equals("/library/genres"));

        then(genreService).should().deleteById(1L);
    }

    @Test
    public void testUnauthenticatedUserCannotAccessAllGenres() throws Exception {
        mockMvc.perform(get("/library/genres"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }

    @Test
    public void testUnauthenticatedUserCannotAccessNewGenreForm() throws Exception {
        mockMvc.perform(get("/library/genres/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }

    @Test
    public void testUnauthenticatedUserCannotSaveGenre() throws Exception {
        mockMvc.perform(post("/library/genres/save")
                        .param("id", "1")
                        .param("name", "Science Fiction"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }

    @Test
    public void testUnauthenticatedUserCannotAccessEditGenreForm() throws Exception {
        mockMvc.perform(get("/library/genres/edit/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }

    @Test
    public void testUnauthenticatedUserCannotDeleteGenre() throws Exception {
        mockMvc.perform(post("/library/genres/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }
}