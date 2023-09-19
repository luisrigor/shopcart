package com.gsc.shopcart.service;

import com.gsc.shopcart.dto.GetOrderStateDTO;
import com.gsc.shopcart.dto.OrderStateDTO;
import com.gsc.shopcart.security.UserPrincipal;

import java.util.List;
import java.util.Map;

public interface OrderStateService {
    OrderStateDTO getOrderState(UserPrincipal userPrincipal, GetOrderStateDTO getOrderStateDTO);

    Map sendInvoice(UserPrincipal userPrincipal, List<Integer> idOrders, Integer idApplication);
}
