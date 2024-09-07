package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ru.otus.hw.domain.Order;
import ru.otus.hw.domain.Product;
import ru.otus.hw.domain.Shipping;

@Slf4j
public class OrderProcessingServiceTest {

    @InjectMocks
    private OrderProcessingServiceImpl orderProcessingService;

    public OrderProcessingServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessOrder() {
        Order order = new Order("1", new Product("1", "product1", 10), 3);
        Shipping shipping = orderProcessingService.processOrder(order);

        assertNotNull(shipping);
        assertEquals("TRACK123456", shipping.getTrackingNumber());
        assertEquals("Shipped", shipping.getStatus());
        assertEquals(order, shipping.getOrder());
    }
}