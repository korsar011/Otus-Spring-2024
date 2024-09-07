package ru.otus.hw.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

import ru.otus.hw.domain.Order;
import ru.otus.hw.services.OrderProcessingService;
import ru.otus.hw.services.ProductService;
import ru.otus.hw.services.ShippingService;


@Configuration
@Slf4j
public class IntegrationConfig {

    @Bean
    public DirectChannel orderChannel() {
        return new DirectChannel();
    }

    @Bean
    public DirectChannel shippingChannel() {
        return new DirectChannel();
    }

    @Bean
    public QueueChannel completedOrdersChannel() {
        return new QueueChannel();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(1);
    }

    @Bean
    public IntegrationFlow orderProcessingFlow(ProductService productService,
                                               OrderProcessingService orderProcessingService) {
        return IntegrationFlow.from(orderChannel())
                .<Order>filter(order -> productService.isProductInStock(order.getProduct(), 1))
                .handle(orderProcessingService, "processOrder")
                .channel(shippingChannel())
                .get();
    }

    @Bean
    public IntegrationFlow shippingFlow(ShippingService shippingService) {
        return IntegrationFlow.from(shippingChannel())
                .handle(shippingService, "handleShipping")
                .get();
    }
}