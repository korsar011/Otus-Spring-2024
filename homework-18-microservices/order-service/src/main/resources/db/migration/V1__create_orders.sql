CREATE TABLE orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        product_id BIGINT NOT NULL,
                        total_price DECIMAL(10, 2) NOT NULL
);


INSERT INTO orders (user_id, product_id, total_price) VALUES
                                                          (1, 1, 99.99),
                                                          (2, 2, 49.99);