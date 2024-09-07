package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import ru.otus.hw.domain.Order;
import ru.otus.hw.domain.Product;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ForkJoinPool;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderGateway orderGateway;

    public OrderServiceImpl(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @Override
    public void processOrders() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (int i = 0; i < 5; i++) {
            int num = i + 1;
            pool.execute(() -> {
                Collection<Order> orders = generateOrders();
                log.info("{}, New orders: {}", num,
                        orders.stream().map(Order::getId).reduce((a, b) -> a + ", " + b).orElse(""));
                orders.forEach(orderGateway::processOrder);
            });
            delay();
        }
    }

    private Collection<Order> generateOrders() {
        return Arrays.asList(
                new Order("1", new Product("1", "product1", 1), 3),
                new Order("2", new Product("2", "product2", 1), 5)
        );
    }

    private void delay() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}