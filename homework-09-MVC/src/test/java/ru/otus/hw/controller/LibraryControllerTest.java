package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(LibraryController.class)
public class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    @Test
    public void testSaveNewBook() throws Exception {
        BookDto bookDto = new BookDto(null, "New Book Title", 1L, 1L);
        Book savedBook = new Book(1L, bookDto.getTitle(), new Author(bookDto.getAuthorId(), null),
                new Genre(bookDto.getGenreId(), null));

        when(bookService.insert(anyString(), anyLong(), anyLong())).thenReturn(savedBook);

        mockMvc.perform(MockMvcRequestBuilders.post("/library/books/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", bookDto.getTitle())
                        .param("authorId", String.valueOf(bookDto.getAuthorId()))
                        .param("genreId", String.valueOf(bookDto.getGenreId())))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/library/books"));

        verify(bookService, times(1)).insert(anyString(), anyLong(), anyLong());
    }

    @Test
    public void testEditBookForm() throws Exception {
        Book book = new Book(1L, "Book Title", new Author(), new Genre());
        BookDto bookDto = BookDto.fromDomainObject(book);

        when(bookService.findById(anyLong())).thenReturn(java.util.Optional.of(book));
        when(authorService.findAll()).thenReturn(List.of(new Author(1L, "Author Name")));
        when(genreService.findAll()).thenReturn(List.of(new Genre(1L, "Genre Name")));

        mockMvc.perform(MockMvcRequestBuilders.get("/library/books/edit")
                        .param("id", String.valueOf(book.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("book_form"))
                .andExpect(MockMvcResultMatchers.model().attribute("book", bookDto))
                .andExpect(MockMvcResultMatchers.model().attribute("authors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.model().attribute("genres", hasSize(1)));
    }

    @Test
    public void testSaveNewAuthor() throws Exception {
        Author author = new Author(0L, "New Author");
        Author savedAuthor = new Author(1L, "New Author");

        when(authorService.save(any(Author.class))).thenReturn(savedAuthor);

        mockMvc.perform(MockMvcRequestBuilders.post("/library/authors")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName", author.getFullName()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/library/authors"));

        verify(authorService, times(1)).save(any(Author.class));
    }

    @Test
    public void testEditAuthorForm() throws Exception {
        Author author = new Author(1L, "Author Name");

        when(authorService.findById(anyLong())).thenReturn(author);

        mockMvc.perform(MockMvcRequestBuilders.get("/library/authors/edit")
                        .param("id", String.valueOf(author.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("author_form"))
                .andExpect(MockMvcResultMatchers.model().attribute("author", author));
    }

    @Test
    public void testSaveNewGenre() throws Exception {
        Genre genre = new Genre(null, "New Genre");

        doNothing().when(genreService).save(any(Genre.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/library/genres/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", genre.getName()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/library/genres"));

        verify(genreService, times(1)).save(any(Genre.class));
    }

    @Test
    public void testEditGenreForm() throws Exception {
        Genre genre = new Genre(1L, "Genre Name");

        when(genreService.findById(anyLong())).thenReturn(java.util.Optional.of(genre));

        mockMvc.perform(MockMvcRequestBuilders.get("/library/genres/edit")
                        .param("id", String.valueOf(genre.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("genre_form"))
                .andExpect(MockMvcResultMatchers.model().attribute("genre", genre));
    }

    @Test
    public void testFindAllCommentsByBookId() throws Exception {
        Comment comment = new Comment(1L, "Great book!", new Book());
        List<Comment> comments = List.of(comment);

        when(commentService.findAllCommentsByBookId(anyLong())).thenReturn(comments);

        mockMvc.perform(MockMvcRequestBuilders.get("/library/comments/by-book/{bookId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("comment_list"))
                .andExpect(MockMvcResultMatchers.model().attribute("comments", hasSize(1)))
                .andExpect(MockMvcResultMatchers.model().attribute("comments", hasItem(comment)));
    }
}