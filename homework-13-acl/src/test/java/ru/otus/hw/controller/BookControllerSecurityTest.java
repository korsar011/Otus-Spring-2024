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
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
@TestPropertySource(properties = {
        "spring.security.user.details-service.enabled=false"
})
public class BookControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private UserDetailsService userDetailsService;

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @ParameterizedTest
    @CsvSource({
            "/library/books, 200",
            "/library/books/new, 200",
            "/library/books/edit/1, 200"
    })
    public void testAuthenticatedUserCanAccessPages(String url, int expectedStatus) throws Exception {
        if (url.equals("/library/books/edit/1")) {
            Book book = new Book(1L, "Test Book Title", new Author(), new Genre());
            given(bookService.findById(1L)).willReturn(Optional.of(book));
            given(authorService.findAll()).willReturn(Collections.singletonList(new Author(1L, "Author Name")));
            given(genreService.findAll()).willReturn(Collections.singletonList(new Genre(1L, "Genre Name")));
        } else if (url.equals("/library/books/new")) {
            given(authorService.findAll()).willReturn(Collections.singletonList(new Author()));
            given(genreService.findAll()).willReturn(Collections.singletonList(new Genre()));
        }

        mockMvc.perform(get(url))
                .andExpect(status().is(expectedStatus));
    }

    @ParameterizedTest
    @CsvSource({
            "/library/books, 302",
            "/library/books/new, 302",
            "/library/books/edit/1, 302"
    })
    public void testUnauthenticatedUserCannotAccessPages(String url, int expectedStatus) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @ParameterizedTest
    @CsvSource({
            "/library/books/edit, id=1&title=Book Title&authorId=1&genreId=1, 302",
            "/library/books/delete/1, null, 302"
    })
    public void testAuthenticatedUserCanPerformActions(String url, String params, int expectedStatus) throws Exception {
        if (url.contains("/delete")) {
            mockMvc.perform(post(url))
                    .andExpect(status().is(expectedStatus))
                    .andExpect(result -> result.getResponse().getRedirectedUrl().equals("/library/books"));
        } else if (url.contains("/edit")) {
            String[] paramArray = params.split("&");
            mockMvc.perform(post(url)
                            .param("id", paramArray[0].split("=")[1])
                            .param("title", paramArray[1].split("=")[1])
                            .param("authorId", paramArray[2].split("=")[1])
                            .param("genreId", paramArray[3].split("=")[1]))
                    .andExpect(status().is(expectedStatus))
                    .andExpect(result -> result.getResponse().getRedirectedUrl().equals("/library/books"));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "/library/books/edit, id=1&title=Book Title&authorId=1&genreId=1, 302",
            "/library/books/delete/1, null, 302"
    })
    public void testUnauthenticatedUserCannotPerformActions(String url, String params, int expectedStatus) throws Exception {
        mockMvc.perform(post(url)
                        .param("id", params.contains("id") ? params.split("&")[0].split("=")[1] : "")
                        .param("title", params.contains("title") ? params.split("&")[1].split("=")[1] : "")
                        .param("authorId", params.contains("authorId") ? params.split("&")[2].split("=")[1] : "")
                        .param("genreId", params.contains("genreId") ? params.split("&")[3].split("=")[1] : ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }
}