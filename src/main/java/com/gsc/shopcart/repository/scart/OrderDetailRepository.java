package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Integer> {
}
