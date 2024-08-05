package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;

@Component
public class AuthorConverter {
    public String authorToString(Author author) {
        return String.format("Id: %s, FullName: %s", author.getId(), author.getFullName());
    }
}