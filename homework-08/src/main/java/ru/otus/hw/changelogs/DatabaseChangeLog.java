package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeLog
public class DatabaseChangeLog {

    @ChangeSet(order = "001", id = "initialSetup", author = "korsar011")
    public void initialSetup(AuthorRepository authorRepository,
                             BookRepository bookRepository, GenreRepository genreRepository) {
        Genre fiction = new Genre("Fiction");
        Genre nonFiction = new Genre("Non-Fiction");
        genreRepository.saveAll(List.of(fiction, nonFiction));

        Author author1 = new Author("Author One");
        Author author2 = new Author("Author Two");
        authorRepository.saveAll(List.of(author1, author2));

        Book book1 = new Book("Book One", author1, fiction);
        Book book2 = new Book("Book Two", author2, nonFiction);
        bookRepository.saveAll(List.of(book1, book2));
    }
}