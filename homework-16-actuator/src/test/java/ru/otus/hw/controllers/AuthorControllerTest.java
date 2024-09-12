package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;

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

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private AuthorMapper authorMapper;

    @Test
    public void testFindAllAuthors() throws Exception {
        Author author = new Author(1L, "Author Name");
        AuthorDto authorDto = new AuthorDto(1L, "Author Name");
        when(authorService.findAll()).thenReturn(List.of(author));
        when(authorMapper.toDto(author)).thenReturn(authorDto);

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fullName").value("Author Name"));
    }

    @Test
    public void testFindAuthorById() throws Exception {
        Author author = new Author(1L, "Author Name");
        AuthorDto authorDto = new AuthorDto(1L, "Author Name");

        when(authorService.findById(1L)).thenReturn(author);
        when(authorMapper.toDto(author)).thenReturn(authorDto);

        mockMvc.perform(get("/api/authors/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Author Name"));
    }

    @Test
    public void testFindAuthorById_NotFound() throws Exception {
        when(authorService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/authors/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateAuthor() throws Exception {
        AuthorDto authorDto = new AuthorDto(null, "New Author");
        Author author = new Author(1L, "New Author");
        AuthorDto savedAuthorDto = new AuthorDto(1L, "New Author");

        when(authorMapper.toEntity(any(AuthorDto.class))).thenReturn(author);
        when(authorService.save(any(Author.class))).thenReturn(author);
        when(authorMapper.toDto(author)).thenReturn(savedAuthorDto);

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"New Author\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("New Author"));
    }

    @Test
    public void testUpdateAuthor() throws Exception {
        Author author = new Author(1L, "Old Name");
        AuthorDto authorDto = new AuthorDto(1L, "Updated Name");
        Author updatedAuthor = new Author(1L, "Updated Name");

        when(authorService.findById(1L)).thenReturn(author);
        when(authorService.save(any(Author.class))).thenReturn(updatedAuthor);
        when(authorMapper.toDto(updatedAuthor)).thenReturn(authorDto);

        mockMvc.perform(put("/api/authors/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"Updated Name\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Updated Name"));
    }

    @Test
    public void testUpdateAuthor_NotFound() throws Exception {
        when(authorService.findById(1L)).thenReturn(null);

        mockMvc.perform(put("/api/authors/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"Updated Name\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        doNothing().when(authorService).deleteById(1L);

        mockMvc.perform(delete("/api/authors/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(authorService, times(1)).deleteById(1L);
    }
}
