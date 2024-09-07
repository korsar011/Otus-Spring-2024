package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import ru.otus.hw.domain.Shipping;

@Service
@Slf4j
public class ShippingServiceImpl implements ShippingService {

    @Override
    public void handleShipping(Shipping shipping) {
        log.info("Shipping details: Order ID: {}, Tracking Number: {}, Status: {}",
                shipping.getOrder().getId(), shipping.getTrackingNumber(), shipping.getStatus());
    }
}