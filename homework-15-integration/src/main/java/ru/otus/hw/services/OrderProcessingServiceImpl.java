package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import ru.otus.hw.domain.Order;
import ru.otus.hw.domain.Shipping;

@Service
@Slf4j
public class OrderProcessingServiceImpl implements OrderProcessingService {

    @Override
    public Shipping processOrder(Order order) {
        log.info("Processing order: {}", order.getId());
        delay();
        return new Shipping(order, "TRACK123456", "Shipped");
    }

    private void delay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}