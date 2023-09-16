package com.gsc.shopcart.sample.data.provider;

import com.gsc.shopcart.dto.GetOrderStateDTO;
import com.gsc.shopcart.dto.OrderStateDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class OrderData {

    public static OrderStateDTO getOrderStatusDTO() {
        return OrderStateDTO.builder()
                .dealerList(new ArrayList<>())
                .hsmDealers(new HashMap<>())
                .orderList(new ArrayList<>())
                .orderStatusList(new ArrayList<>())
                .hsmOrderDetails(new HashMap<>())
                .suppliers(new HashMap<>())
                .users(new LinkedHashMap<>())
                .idCatalog(1)
                .preferences(new HashMap<>())
                .idApplication(2)
                .build();
    }
    public static GetOrderStateDTO getGetOrderStateDTO(){
        return GetOrderStateDTO.builder()
                .idCatalog(1)
                .idProfileTcap(2)
                .idProfileSupplier(3)
                .idOrderStatus(4)
                .idSupplier(5)
                .idUser(6)
                .idApplication(7)
                .orderNr(8)
                .iPec("IPecValue")
                .reference("ReferenceValue")
                .oidParent("OidParentValue")
                .orderType("OrderTypeValue")
                .build();
    }
}
