package com.gsc.shopcart.service;

import com.gsc.shopcart.dto.GetOrderStateDTO;
import com.gsc.shopcart.dto.OrderStatusDTO;
import com.gsc.shopcart.security.UserPrincipal;

public interface OrderStatusService {
    OrderStatusDTO getOrderState(UserPrincipal userPrincipal, GetOrderStateDTO getOrderStateDTO);
}
