package ru.otus.orderservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import ru.otus.orderservice.client.ProductClient;
import ru.otus.orderservice.client.UserClient;
import ru.otus.orderservice.model.Order;
import ru.otus.orderservice.model.ProductVO;
import ru.otus.orderservice.model.UserVO;
import ru.otus.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final UserClient userClient;

    private final ProductClient productClient;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserClient userClient, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.userClient = userClient;
        this.productClient = productClient;
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackUser")
    public UserVO getUserById(Long userId) {
        return userClient.getUserById(userId);
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProduct")
    public ProductVO getProductById(Long productId) {
        return productClient.getProductById(productId);
    }

    public Order createOrder(Order order) {
        UserVO user = getUserById(order.getUserId());
        ProductVO product = getProductById(order.getProductId());

        if (user != null) {
            order.setUserName(user.getName());
        } else {
            order.setUserName("Unknown User");
        }

        if (product != null) {
            order.setProductName(product.getName());
            order.setTotalPrice(product.getPrice());
        } else {
            order.setProductName("Unknown Product");
            order.setTotalPrice(0.0);
        }

        return orderRepository.save(order);
    }

    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackGetOrderById")
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public UserVO fallbackUser(Long userId, Throwable t) {
        log.info("Fallback for UserVO Service: " + t.getMessage());
        return new UserVO(userId, "Unknown User", "unknown@example.com", "000-000-0000");
    }

    public ProductVO fallbackProduct(Long productId, Throwable t) {
        log.info("Fallback for ProductVO Service: " + t.getMessage());
        return new ProductVO(productId, "Unknown Product", "No description", 0.0);
    }

    public Optional<Order> fallbackGetOrderById(Long id, Throwable t) {
        log.info("Fallback for Order Service: " + t.getMessage());
        return Optional.empty();
    }
}