package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentService commentService;

    @GetMapping
    public String home() {
        return "index";
    }

    @GetMapping("/books")
    public String findAllBooks(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "book_list";
    }

    @GetMapping("/books/edit")
    public String editPage(@RequestParam("id") long id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));
        BookDto bookDto = BookDto.fromDomainObject(book);
        model.addAttribute("book", bookDto);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "book_form";
    }

    @PostMapping("/books/edit")
    public String saveBook(@Valid @ModelAttribute BookDto bookDto,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "book_form";
        }
        if (bookDto.getId() == null) {
            bookService.insert(bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId());
        } else {
            bookService.update(bookDto.getId(), bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId());
        }
        return "redirect:/library/books";
    }

    @GetMapping("/books/new")
    public String newBookForm(Model model) {
        model.addAttribute("book", new BookDto());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "book_form";
    }

    @PostMapping("/books/delete")
    public String deleteBook(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return "redirect:/library/books";
    }

    @GetMapping("/authors")
    public String findAllAuthors(Model model) {
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        return "authors";
    }

    @GetMapping("/authors/new")
    public String newAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "author_form";
    }

    @PostMapping("/authors")
    public String saveAuthor(@Valid @ModelAttribute Author author, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "author_form";
        }
        authorService.save(author);
        return "redirect:/library/authors";
    }

    @GetMapping("/authors/edit")
    public String editAuthorForm(@RequestParam("id") long id, Model model) {
        Author author = authorService.findById(id);
        model.addAttribute("author", author);
        return "author_form";
    }

    @PostMapping("/authors/delete")
    public String deleteAuthor(@RequestParam("id") long id) {
        authorService.deleteById(id);
        return "redirect:/library/authors";
    }

    @GetMapping("/genres")
    public String findAllGenres(Model model) {
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "genre_list";
    }

    @GetMapping("/genres/new")
    public String newGenreForm(Model model) {
        model.addAttribute("genre", new Genre());
        return "genre_form";
    }

    @PostMapping("/genres/save")
    public String saveGenre(@Valid @ModelAttribute Genre genre, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "genre_form";
        }
        genreService.save(genre);
        return "redirect:/library/genres";
    }

    @GetMapping("/genres/edit")
    public String editGenreForm(@RequestParam("id") long id, Model model) {
        Genre genre = genreService.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Genre with id " + id + " not found"));
        model.addAttribute("genre", genre);
        return "genre_form";
    }

    @PostMapping("/genres/delete")
    public String deleteGenre(@RequestParam("id") long id) {
        genreService.deleteById(id);
        return "redirect:/library/genres";
    }

    @GetMapping("/comments/by-book/{bookId}")
    public String findAllCommentsByBookId(@PathVariable("bookId") long bookId, Model model) {
        List<Comment> comments = commentService.findAllCommentsByBookId(bookId);
        if (comments.isEmpty()) {
            model.addAttribute("message", "No comments found for the specified book.");
        } else {
            model.addAttribute("comments", comments);
        }
        return "comment_list";
    }

}