package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.dto.entity.GetOrderStateDTO;
import com.gsc.shopcart.model.scart.entity.Order;
import com.gsc.shopcart.security.UserPrincipal;
import java.util.List;

public interface OrderCustomRepository {

    List<Order> getOrderByCriteria(GetOrderStateDTO getOrderStateDTO, UserPrincipal userPrincipal, StringBuilder criteria, StringBuilder criteriaDetail);

}
