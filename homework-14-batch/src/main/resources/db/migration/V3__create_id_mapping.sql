-- Миграция для создания таблицы id_map

CREATE TABLE id_map (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        sql_id BIGINT NOT NULL,
                        mongo_id VARCHAR(255) NOT NULL,
                        entity_type VARCHAR(50) NOT NULL
);

-- Индекс для быстрого поиска по sql_id и entity_type
CREATE INDEX idx_sql_id_entity_type ON id_map (sql_id, entity_type);