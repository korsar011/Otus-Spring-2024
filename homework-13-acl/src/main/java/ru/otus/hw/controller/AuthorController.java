package ru.otus.hw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import jakarta.validation.Valid;

@Controller
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/library/authors")
    public String findAllAuthors(Model model) {
        model.addAttribute("authors", authorService.findAll());
        return "authors";
    }

    @GetMapping("/library/authors/new")
    public String newAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "author_form";
    }

    @PostMapping("/library/authors")
    public String saveAuthor(@Valid Author author, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "author_form";
        }
        authorService.save(author);
        return "redirect:/library/authors";
    }

    @GetMapping("/library/authors/edit/{id}")
    public String editAuthorForm(@PathVariable("id") long id, Model model) {
        Author author = authorService.findById(id);
        model.addAttribute("author", author);
        return "author_form";
    }

    @PostMapping("/library/authors/delete/{id}")
    public String deleteAuthor(@PathVariable("id") long id) {
        authorService.deleteById(id);
        return "redirect:/library/authors";
    }
}