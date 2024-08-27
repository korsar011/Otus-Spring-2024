package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "genre.id", target = "genreId")
    BookDto toDto(Book book);

    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "genreId", target = "genre.id")
    Book toEntity(BookDto bookDto);

    default Author toAuthor(String authorId) {
        if (authorId == null) {
            return null;
        }
        Author author = new Author();
        author.setId(authorId);
        return author;
    }

    default Genre toGenre(String genreId) {
        if (genreId == null) {
            return null;
        }
        Genre genre = new Genre();
        genre.setId(genreId);
        return genre;
    }
}
