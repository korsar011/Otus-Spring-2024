package ru.otus.hw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import jakarta.validation.Valid;
import java.util.List;

@Controller
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/library/genres")
    public String findAllGenres(Model model) {
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "genre_list";
    }

    @GetMapping("/library/genres/new")
    public String newGenreForm(Model model) {
        model.addAttribute("genre", new Genre());
        return "genre_form";
    }

    @PostMapping("/library/genres/save")
    public String saveGenre(@Valid Genre genre, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "genre_form";
        }
        genreService.save(genre);
        return "redirect:/library/genres";
    }

    @GetMapping("/library/genres/edit/{id}")
    public String editGenreForm(@PathVariable("id") long id, Model model) {
        Genre genre = genreService.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Genre with id " + id + " not found"));
        model.addAttribute("genre", genre);
        return "genre_form";
    }

    @PostMapping("/library/genres/delete/{id}")
    public String deleteGenre(@PathVariable("id") long id) {
        genreService.deleteById(id);
        return "redirect:/library/genres";
    }
}