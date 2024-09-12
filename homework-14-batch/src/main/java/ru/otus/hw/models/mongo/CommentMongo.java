package ru.otus.hw.models.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Data
@NoArgsConstructor
@Document(collection = "comments")
public class CommentMongo {
    @Id
    private String id;

    private String content;

    @DBRef
    private BookMongo book;

    public CommentMongo(String id, String content, BookMongo book) {
        this.id = id;
        this.content = content;
        this.book = book;
    }

    public CommentMongo(String content, BookMongo book) {
        this.content = content;
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentMongo comment)) {
            return false;
        }
        return Objects.equals(getId(), comment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "CommentMongo{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}