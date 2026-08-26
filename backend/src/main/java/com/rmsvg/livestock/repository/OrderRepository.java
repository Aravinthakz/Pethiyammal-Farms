package com.rmsvg.livestock.repository;

import com.rmsvg.livestock.domain.Enums.OrderStatus;
import com.rmsvg.livestock.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
    long countByOrderStatus(OrderStatus status);
    List<CustomerOrder> findByCustomerIdOrderByOrderDateDesc(Long customerId);
    List<CustomerOrder> findByOrderStatus(OrderStatus status);
}
