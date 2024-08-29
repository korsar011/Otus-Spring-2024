package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CustomUserDetailsService;
import ru.otus.hw.services.GenreService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
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
    private CustomUserDetailsService customUserDetailsService;

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    public void testAuthenticatedUserCanAccessBooksPage() throws Exception {
        mockMvc.perform(get("/library/books"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnauthenticatedUserCannotAccessBooksPage() throws Exception {
        mockMvc.perform(get("/library/books"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    public void testAuthenticatedUserCanAccessNewBookForm() throws Exception {
        given(authorService.findAll()).willReturn(Collections.singletonList(new Author()));
        given(genreService.findAll()).willReturn(Collections.singletonList(new Genre()));

        mockMvc.perform(get("/library/books/new"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnauthenticatedUserCannotAccessNewBookForm() throws Exception {
        mockMvc.perform(get("/library/books/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    public void testAuthenticatedUserCanAccessEditBookForm() throws Exception {
        Book book = new Book(1L, "Test Book Title", new Author(), new Genre());
        given(bookService.findById(1L)).willReturn(Optional.of(book));
        given(authorService.findAll()).willReturn(Collections.singletonList(new Author(1L, "Author Name")));
        given(genreService.findAll()).willReturn(Collections.singletonList(new Genre(1L, "Genre Name")));

        mockMvc.perform(get("/library/books/edit/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnauthenticatedUserCannotAccessEditBookForm() throws Exception {
        mockMvc.perform(get("/library/books/edit/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    public void testAuthenticatedUserCanSaveBook() throws Exception {
        mockMvc.perform(post("/library/books/edit")
                        .param("id", "1")
                        .param("title", "Book Title")
                        .param("authorId", "1")
                        .param("genreId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().equals("/library/books"));
    }

    @Test
    public void testUnauthenticatedUserCannotSaveBook() throws Exception {
        mockMvc.perform(post("/library/books/edit")
                        .flashAttr("book", new BookDto(1L, "Book Title", 1L, 1L)))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    public void testAuthenticatedUserCanDeleteBook() throws Exception {
        mockMvc.perform(post("/library/books/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().equals("/library/books"));
    }

    @Test
    public void testUnauthenticatedUserCannotDeleteBook() throws Exception {
        mockMvc.perform(post("/library/books/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }
}