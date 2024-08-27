package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.any;
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

@WebMvcTest(GenreController.class)
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @MockBean
    private GenreMapper genreMapper;

    @Test
    public void testFindAllGenres() throws Exception {
        Genre genre = new Genre(1L, "Fiction");
        GenreDto genreDto = new GenreDto(1L, "Fiction");

        when(genreService.findAll()).thenReturn(List.of(genre));
        when(genreMapper.toDto(genre)).thenReturn(genreDto);

        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Fiction"));
    }

    @Test
    public void testFindGenreById() throws Exception {
        Genre genre = new Genre(1L, "Fiction");
        GenreDto genreDto = new GenreDto(1L, "Fiction");

        when(genreService.findById(1L)).thenReturn(Optional.of(genre));
        when(genreMapper.toDto(genre)).thenReturn(genreDto);

        mockMvc.perform(get("/api/genres/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Fiction"));
    }

    @Test
    public void testFindGenreById_NotFound() throws Exception {
        when(genreService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/genres/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateGenre() throws Exception {
        Genre genre = new Genre(null, "New Genre");
        GenreDto savedGenreDto = new GenreDto(1L, "New Genre");


        when(genreMapper.toEntity(any(GenreDto.class))).thenReturn(genre);
        when(genreMapper.toDto(any(Genre.class))).thenReturn(savedGenreDto);

        mockMvc.perform(post("/api/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Genre\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Genre"));

        verify(genreService, times(1)).save(any(Genre.class));
    }

    @Test
    public void testUpdateGenre() throws Exception {
        long genreId = 1L;
        GenreDto genreDto = new GenreDto(genreId, "Updated Genre");
        Genre existingGenre = new Genre(genreId, "Old Genre");
        Genre updatedGenre = new Genre(genreId, "Updated Genre");

        when(genreService.findById(genreId)).thenReturn(Optional.of(existingGenre));
        when(genreMapper.toEntity(genreDto)).thenReturn(updatedGenre);
        when(genreMapper.toDto(updatedGenre)).thenReturn(genreDto);

        mockMvc.perform(put("/api/genres/{id}", genreId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Genre\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(genreId))
                .andExpect(jsonPath("$.name").value("Updated Genre"));

        verify(genreService, times(1)).save(any(Genre.class));
    }


    @Test
    public void testDeleteGenre() throws Exception {
        doNothing().when(genreService).deleteById(1L);

        mockMvc.perform(delete("/api/genres/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(genreService, times(1)).deleteById(1L);
    }
}
