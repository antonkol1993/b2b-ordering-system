package com.yourapp.order.repository;

import com.yourapp.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByTenant_Id(Long tenantId);

    Optional<Order> findByIdAndTenant_Id(Long id, Long tenantId);
}

