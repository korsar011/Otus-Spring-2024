package ru.otus.hw.converter;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.hw.mapper.SqlToNosqlMapper;
import ru.otus.hw.models.IdMap;
import ru.otus.hw.models.h2.Author;
import ru.otus.hw.models.h2.Book;
import ru.otus.hw.models.h2.Comment;
import ru.otus.hw.models.h2.Genre;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.models.mongo.CommentMongo;
import ru.otus.hw.models.mongo.GenreMongo;
import ru.otus.hw.repositories.IdMapRepository;
import ru.otus.hw.repositories.mongo.AuthorMongoRepository;
import ru.otus.hw.repositories.mongo.BookMongoRepository;
import ru.otus.hw.repositories.mongo.CommentMongoRepository;
import ru.otus.hw.repositories.mongo.GenreMongoRepository;

@Component
public class EntityConverter {

    private final SqlToNosqlMapper sqlToNosqlMapper;

    private final IdMapRepository idMapRepository;

    private final AuthorMongoRepository authorMongoRepository;

    private final GenreMongoRepository genreMongoRepository;

    private final BookMongoRepository bookMongoRepository;

    private final CommentMongoRepository commentMongoRepository;

    @Autowired
    public EntityConverter(SqlToNosqlMapper sqlToNosqlMapper,
                           IdMapRepository idMapRepository,
                           AuthorMongoRepository authorMongoRepository,
                           GenreMongoRepository genreMongoRepository,
                           BookMongoRepository bookMongoRepository,
                           CommentMongoRepository commentMongoRepository) {
        this.sqlToNosqlMapper = sqlToNosqlMapper;
        this.idMapRepository = idMapRepository;
        this.authorMongoRepository = authorMongoRepository;
        this.genreMongoRepository = genreMongoRepository;
        this.bookMongoRepository = bookMongoRepository;
        this.commentMongoRepository = commentMongoRepository;
    }

    public AuthorMongo convertAuthor(Author author) {
        String mongoAuthorId = new ObjectId().toString();
        idMapRepository.save(new IdMap(null, author.getId(), mongoAuthorId, "Author"));
        return new AuthorMongo(mongoAuthorId, author.getFullName());
    }

    public GenreMongo convertGenre(Genre genre) {
        String mongoGenreId = new ObjectId().toString();
        idMapRepository.save(new IdMap(null, genre.getId(), mongoGenreId, "Genre"));
        return new GenreMongo(mongoGenreId, genre.getName());
    }

    public BookMongo convertBook(Book book) {
        String mongoBookId = new ObjectId().toString();
        idMapRepository.save(new IdMap(null, book.getId(), mongoBookId, "Book"));
        return new BookMongo(book.getTitle(),
                new AuthorMongo(idMapRepository.findMongoIdBySqlIdAndEntityType(
                        book.getAuthor().getId(), "Author"), book.getAuthor().getFullName()),
                new GenreMongo(idMapRepository.findMongoIdBySqlIdAndEntityType(
                        book.getGenre().getId(), "Genre"), book.getGenre().getName()));
    }

    public CommentMongo convertComment(Comment comment) {
        String mongoCommentId = new ObjectId().toString();
        idMapRepository.save(new IdMap(null, comment.getId(), mongoCommentId, "Comment"));
        return new CommentMongo(mongoCommentId,
                comment.getContent(),
                new BookMongo(idMapRepository.findMongoIdBySqlIdAndEntityType(
                        comment.getBook().getId(), "Book"), comment.getBook().getTitle(), null, null));
    }
}