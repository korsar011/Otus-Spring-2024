package ru.otus.hw.converter;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.h2.Author;
import ru.otus.hw.models.h2.Book;
import ru.otus.hw.models.h2.Comment;
import ru.otus.hw.models.h2.Genre;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.models.mongo.CommentMongo;
import ru.otus.hw.models.mongo.GenreMongo;
import ru.otus.hw.repositories.inMemory.IdMapRepository;

@Component
public class EntityConverter {

    private final IdMapRepository idMapRepository;

    public EntityConverter(IdMapRepository idMapRepository) {
        this.idMapRepository = idMapRepository;
    }

    public AuthorMongo convertAuthor(Author author) {
        String mongoAuthorId = new ObjectId().toString();
        idMapRepository.save(author.getId(), "Author", mongoAuthorId);
        return new AuthorMongo(mongoAuthorId, author.getFullName());
    }

    public GenreMongo convertGenre(Genre genre) {
        String mongoGenreId = new ObjectId().toString();
        idMapRepository.save(genre.getId(), "Genre", mongoGenreId);
        return new GenreMongo(mongoGenreId, genre.getName());
    }

    public BookMongo convertBook(Book book) {
        String mongoBookId = new ObjectId().toString();
        idMapRepository.save(book.getId(), "Book", mongoBookId);
        return new BookMongo(book.getTitle(),
                new AuthorMongo(idMapRepository.findMongoIdBySqlIdAndType(
                        book.getAuthor().getId(), "Author"), book.getAuthor().getFullName()),
                new GenreMongo(idMapRepository.findMongoIdBySqlIdAndType(
                        book.getGenre().getId(), "Genre"), book.getGenre().getName()));
    }

    public CommentMongo convertComment(Comment comment) {
        String mongoCommentId = new ObjectId().toString();
        idMapRepository.save(comment.getId(), "Comment", mongoCommentId);
        return new CommentMongo(mongoCommentId,
                comment.getContent(),
                new BookMongo(idMapRepository.findMongoIdBySqlIdAndType(
                        comment.getBook().getId(), "Book"), comment.getBook().getTitle(), null, null));
    }
}