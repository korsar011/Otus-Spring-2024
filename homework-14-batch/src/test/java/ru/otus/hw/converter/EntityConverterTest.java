package ru.otus.hw.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.models.h2.Author;
import ru.otus.hw.models.h2.Book;
import ru.otus.hw.models.h2.Genre;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.repositories.IdMapRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class EntityConverterTest {

    @Mock
    private IdMapRepository idMapRepository;

    @InjectMocks
    private EntityConverter entityConverter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConvertAuthor() {
        Author author = new Author(1L, "Author Name");
        when(idMapRepository.save(any())).thenReturn(null);

        AuthorMongo result = entityConverter.convertAuthor(author);

        assertEquals("Author Name", result.getFullName());
        assertEquals(24, result.getId().length());
    }

    @Test
    public void testConvertBook() {
        Author author = new Author(1L, "Author Name");
        Genre genre = new Genre(1L, "Genre Name");
        Book book = new Book(1L, "Book Title", author, genre);

        when(idMapRepository.findMongoIdBySqlIdAndEntityType(1L, "Author")).thenReturn("mongoAuthorId");
        when(idMapRepository.findMongoIdBySqlIdAndEntityType(1L, "Genre")).thenReturn("mongoGenreId");
        when(idMapRepository.save(any())).thenReturn(null);

        BookMongo result = entityConverter.convertBook(book);

        assertEquals("Book Title", result.getTitle());
        assertEquals("mongoAuthorId", result.getAuthor().getId());
        assertEquals("mongoGenreId", result.getGenre().getId());
    }
}