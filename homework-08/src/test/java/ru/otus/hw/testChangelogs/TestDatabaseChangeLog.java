package ru.otus.hw.testChangelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeLog
public class TestDatabaseChangeLog {

    @ChangeSet(order = "001", id = "dropDb", author = "Adam", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "testDataSetup", author = "korsar011", runAlways = true)
    public void testDataSetup(AuthorRepository authorRepository, BookRepository bookRepository, GenreRepository genreRepository) {
        Genre mystery = new Genre("Mystery");
        Genre scienceFiction = new Genre("Science Fiction");
        genreRepository.saveAll(List.of(mystery, scienceFiction));

        Author author1 = new Author("Test Author One");
        Author author2 = new Author("Test Author Two");
        Author author3 = new Author("Test Author Three");
        authorRepository.saveAll(List.of(author1, author2, author3));

        Book book1 = new Book("Test Book One", author1, mystery);
        Book book2 = new Book("Test Book Two", author2, scienceFiction);
        Book book3 = new Book("Test Book Three", author3, mystery);
        bookRepository.saveAll(List.of(book1, book2, book3));
    }
}