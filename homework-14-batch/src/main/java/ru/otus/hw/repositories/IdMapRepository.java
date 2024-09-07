package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.IdMap;

import java.util.List;

@Repository
public interface IdMapRepository extends JpaRepository<IdMap, Long> {

    @Query("SELECT im.mongoId FROM IdMap im WHERE im.sqlId = :sqlId AND im.entityType = :entityType")
    String findMongoIdBySqlIdAndEntityType(@Param("sqlId") Long sqlId, @Param("entityType") String entityType);

    @Query("SELECT im.mongoId FROM IdMap im WHERE im.sqlId = :sqlId AND im.entityType = :entityType")
    List<String> findMongoIdsBySqlIdAndEntityType(@Param("sqlId") Long sqlId, @Param("entityType") String entityType);
}