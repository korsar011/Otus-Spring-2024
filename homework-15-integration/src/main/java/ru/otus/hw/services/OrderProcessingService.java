package ru.otus.hw.services;

import ru.otus.hw.domain.Order;
import ru.otus.hw.domain.Shipping;

public interface OrderProcessingService {
    Shipping processOrder(Order order);
}