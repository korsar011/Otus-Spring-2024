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
import ru.otus.hw.models.Genre;
import ru.otus.hw.security.SecurityConfiguration;
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
@TestPropertySource(properties = {
        "spring.security.user.details-service.enabled=false"
})
public class GenreControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @MockBean
    private UserDetailsService userDetailsService;

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @ParameterizedTest
    @CsvSource({
            "1, Science Fiction, true",
            "1, , false"
    })
    public void testAuthenticatedUserCanAccessAllGenres(Long id, String name, boolean hasGenre) throws Exception {
        if (hasGenre) {
            Genre genre = new Genre(id, name);
            given(genreService.findAll()).willReturn(Collections.singletonList(genre));
            mockMvc.perform(get("/library/genres"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("genres"))
                    .andExpect(model().attribute("genres", Collections.singletonList(genre)));
        } else {
            given(genreService.findAll()).willReturn(Collections.emptyList());
            mockMvc.perform(get("/library/genres"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("genres"))
                    .andExpect(model().attribute("genres", Collections.emptyList()));
        }
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @ParameterizedTest
    @CsvSource({
            "1, Science Fiction",
            "2, Fantasy"
    })
    public void testAuthenticatedUserCanAccessEditGenreForm(Long id, String name) throws Exception {
        Genre genre = new Genre(id, name);
        given(genreService.findById(id)).willReturn(Optional.of(genre));

        mockMvc.perform(get("/library/genres/edit/{id}", id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("genre"))
                .andExpect(model().attribute("genre", genre));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @ParameterizedTest
    @CsvSource({
            "1, Science Fiction",
            "2, Fantasy"
    })
    public void testAuthenticatedUserCanSaveGenre(Long id, String name) throws Exception {
        Genre genre = new Genre(id, name);

        mockMvc.perform(post("/library/genres")
                        .param("id", id.toString())
                        .param("name", name))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/library/genres"));

        then(genreService).should().save(genre);
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @ParameterizedTest
    @CsvSource({
            "1",
            "2"
    })
    public void testAuthenticatedUserCanDeleteGenre(Long id) throws Exception {
        mockMvc.perform(post("/library/genres/delete/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().equals("/library/genres"));

        then(genreService).should().deleteById(id);
    }

    @ParameterizedTest
    @CsvSource({
            "/library/genres",
            "/library/genres/new",
            "/library/genres/save",
            "/library/genres/edit/1",
            "/library/genres/delete/1"
    })
    public void testUnauthenticatedUserCannotAccessGenreEndpoints(String url) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }
}