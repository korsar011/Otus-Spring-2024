package ru.otus.hw.repositories;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

public class CustomBookRepositoryImpl implements CustomBookRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    public CustomBookRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<Void> updateAuthorInBooks(String authorId, Author newAuthor) {
        Query query = Query.query(Criteria.where("author.id").is(authorId));
        Update update = Update.update("author", newAuthor);
        return mongoTemplate.updateMulti(query, update, Book.class).then();
    }

    @Override
    public Mono<Void> updateManyByGenreId(String genreId, Genre newGenre) {
        Query query = Query.query(Criteria.where("genre.id").is(genreId));
        Update update = Update.update("genre", newGenre);
        return mongoTemplate.updateMulti(query, update, Book.class).then();
    }
}