package ru.otus.hw.repositories.inMemory;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class IdMapRepository {

    private final Map<String, Map<Long, String>> ids = new HashMap<>();

    public String findMongoIdBySqlIdAndType(Long sqlId, String entityType) {
        Map<Long, String> entityMap = ids.get(entityType);
        if (entityMap != null) {
            return entityMap.get(sqlId);
        }
        return null;
    }

    public void save(Long sqlId, String entityType, String mongoId) {
        ids.computeIfAbsent(entityType, k -> new HashMap<>()).put(sqlId, mongoId);
        System.out.println("Saved: sqlId=" + sqlId + ", entityType=" + entityType + ", mongoId=" + mongoId);
    }
}