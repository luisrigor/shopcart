package com.gsc.shopcart.service;

import com.gsc.shopcart.dto.GetOrderStateDTO;
import com.gsc.shopcart.dto.ListOrderDTO;
import com.gsc.shopcart.dto.OrderStateDTO;
import com.gsc.shopcart.model.scart.entity.OrderDetail;
import com.gsc.shopcart.security.UserPrincipal;

import java.util.List;

public interface OrderStateService {
    OrderStateDTO getOrderState(UserPrincipal userPrincipal, GetOrderStateDTO getOrderStateDTO);

    void sendInvoice(UserPrincipal userPrincipal, List<Integer> idOrders);

    ListOrderDTO listOrderDetail(UserPrincipal user, Integer idOrder, Integer idOrderDetailStatus);

    OrderDetail changeOrderDetailStatus(Integer idOrderDetail);
}
