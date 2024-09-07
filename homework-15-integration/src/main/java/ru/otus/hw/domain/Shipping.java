package ru.otus.hw.domain;

public class Shipping {
    private Order order;

    private String trackingNumber;

    private String status;

    public Shipping(Order order, String trackingNumber, String status) {
        this.order = order;
        this.trackingNumber = trackingNumber;
        this.status = status;
    }

    public Order getOrder() {
        return order;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getStatus() {
        return status;
    }
}