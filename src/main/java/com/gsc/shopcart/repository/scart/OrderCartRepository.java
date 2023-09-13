package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.OrderCart;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderCartRepository extends JpaRepository<OrderCart, Integer>, OrderCartCustomRepository {


}
