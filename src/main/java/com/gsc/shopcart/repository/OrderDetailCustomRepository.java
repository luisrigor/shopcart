package com.gsc.shopcart.repository;

import com.gsc.shopcart.model.scart.entity.OrderDetail;
import com.gsc.shopcart.security.UserPrincipal;

import java.util.List;

public interface OrderDetailCustomRepository {
    List<OrderDetail> getOrderDetailByIdOrder(Integer idOrder, Integer idOrderDetailStatus, UserPrincipal user);
}
