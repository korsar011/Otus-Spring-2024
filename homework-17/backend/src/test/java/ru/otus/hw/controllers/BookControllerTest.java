package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookMapper bookMapper;

    @Test
    public void testFindAllBooks() throws Exception {
        Book book = new Book(1L, "Book Title", new Author(), new Genre());
        BookDto bookDto = new BookDto(1L, "Book Title", 1L, 1L);
        when(bookService.findAll()).thenReturn(List.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Book Title"));
    }

    @Test
    public void testFindBookById() throws Exception {
        Book book = new Book(1L, "Book Title", new Author(), new Genre());
        BookDto bookDto = new BookDto(1L, "Book Title", 1L, 1L);

        when(bookService.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        mockMvc.perform(get("/api/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Book Title"));
    }

    @Test
    public void testFindBookById_NotFound() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateBook() throws Exception {
        Book book = new Book(1L, "New Book", new Author(1L, "Author"), new Genre(1L, "Genre"));
        BookDto bookDto = new BookDto(null, "New Book", 1L, 1L);
        BookDto savedBookDto = new BookDto(1L, "New Book", 1L, 1L);

        when(bookService.insert(anyString(), anyLong(), anyLong())).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(savedBookDto);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Book\",\"authorId\":1,\"genreId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Book"));
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book book = new Book(1L, "Old Title", new Author(), new Genre());
        BookDto bookDto = new BookDto(1L, "Updated Title", 1L, 1L);

        when(bookService.findById(1L)).thenReturn(Optional.of(book));
        when(bookService.update(anyLong(), anyString(), anyLong(), anyLong())).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        mockMvc.perform(put("/api/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\",\"authorId\":1,\"genreId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }
    @Test
    public void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteById(1L);

        mockMvc.perform(delete("/api/books/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteById(1L);
    }
}
