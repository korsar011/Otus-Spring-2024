package ru.otus.hw.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.Order;

@MessagingGateway
public interface OrderGateway {
    @Gateway(requestChannel = "orderChannel")
    void processOrder(Order order);
}