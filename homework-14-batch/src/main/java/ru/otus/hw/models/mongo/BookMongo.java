package ru.otus.hw.models.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class BookMongo {

    @Id
    private String id;

    private String title;

    private AuthorMongo author;

    private GenreMongo genre;

    public BookMongo(String title, AuthorMongo author, GenreMongo genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookMongo book)) {
            return false;
        }
        return Objects.equals(getId(), book.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "BookMongo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}