package ru.otus.hw.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.IdMapRepository;

import java.util.List;

@Component
public class SqlToNosqlMapper {

    private final IdMapRepository idMapRepository;

    @Autowired
    public SqlToNosqlMapper(IdMapRepository idMapRepository) {
        this.idMapRepository = idMapRepository;
    }

    public String getMongoBookId(Long sqlBookId) {
        return getMongoId(sqlBookId, "BOOK");
    }

    public String getMongoAuthorId(Long sqlAuthorId) {
        return getMongoId(sqlAuthorId, "AUTHOR");
    }

    public String getMongoGenreId(Long sqlGenreId) {
        return getMongoId(sqlGenreId, "GENRE");
    }

    public String getMongoCommentId(Long sqlCommentId) {
        return getMongoId(sqlCommentId, "COMMENT");
    }

    private String getMongoId(Long sqlId, String entityType) {
        return idMapRepository.findMongoIdBySqlIdAndEntityType(sqlId, entityType);
    }

    public List<String> getMongoIds(List<Long> sqlIds, String entityType) {
        return sqlIds.stream()
                .map(sqlId -> idMapRepository.findMongoIdBySqlIdAndEntityType(sqlId, entityType))
                .toList();
    }
}