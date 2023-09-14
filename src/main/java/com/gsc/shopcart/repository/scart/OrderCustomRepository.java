package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.dto.GetOrderStateDTO;
import com.gsc.shopcart.model.scart.entity.Order;
import com.gsc.shopcart.security.UserPrincipal;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface OrderCustomRepository {

    List<Order> getOrderByCriteria(GetOrderStateDTO getOrderStateDTO, UserPrincipal userPrincipal, StringBuilder criteria, StringBuilder criteriaDetail);

    Map<String, String> getSuppliers(Integer idProfileTcap, Integer idProfileSupplier, String oidNet);

    LinkedHashMap<String, String> getUsersByApplication(String oidNet, String oidDealerParent);




}
