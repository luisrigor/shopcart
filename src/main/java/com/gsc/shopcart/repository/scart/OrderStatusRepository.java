package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
}
