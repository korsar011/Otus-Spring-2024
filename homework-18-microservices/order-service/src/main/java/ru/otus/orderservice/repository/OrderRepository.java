package ru.otus.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.orderservice.model.Order;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}