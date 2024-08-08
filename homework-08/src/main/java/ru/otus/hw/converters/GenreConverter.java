package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Genre;

@Component
public class GenreConverter {
    public String genreToString(Genre genre) {
        if (genre == null) {
            return "Genre not found";
        }
        return "Id: %s, Name: %s".formatted(
                genre.getId(),
                genre.getName());
    }
}
