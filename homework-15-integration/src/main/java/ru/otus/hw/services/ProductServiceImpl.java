package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import ru.otus.hw.domain.Product;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Override
    public boolean isProductInStock(Product product, int quantity) {
        log.info("Checking stock for product: {}, quantity: {}", product.getProductId(), quantity);
        return product.getStock() >= quantity;
    }
}