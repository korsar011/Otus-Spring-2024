package ru.otus.hw.services;

import ru.otus.hw.domain.Product;

public interface ProductService {
    boolean isProductInStock(Product product, int quantity);
}