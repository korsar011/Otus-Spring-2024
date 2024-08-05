package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public String bookToString(Book book) {
        if (book == null) {
            return "Book not found";
        }
        String authorStr = book.getAuthor() != null ?
                authorConverter.authorToString(book.getAuthor()) : "Unknown author";
        String genreStr = book.getGenre() != null ?
                genreConverter.genreToString(book.getGenre()) : "Unknown genre";

        return "Id: %s, Title: %s, Author: {%s}, Genre: {%s}".formatted(
                book.getId(),
                book.getTitle(),
                authorStr,
                genreStr);
    }
}