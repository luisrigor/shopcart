package com.gsc.shopcart.dto;

import com.gsc.shopcart.model.scart.entity.Order;
import com.gsc.shopcart.model.scart.entity.OrderDetail;
import com.gsc.shopcart.model.scart.entity.OrderStatus;
import com.rg.dealer.Dealer;
import lombok.*;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatusDTO {

        private List<Dealer> dealerList;
        private Map<String,Dealer> hsmDealers;
        private List<Order> orderList = new ArrayList<>();
        private List<OrderStatus> orderStatusList = new ArrayList<>();
        private Map<Integer, List<OrderDetail>> hsmOrderDetails;
        private Map<String,String> suppliers;
        private LinkedHashMap<String, String> users;
        private Integer idCatalog;
        private Map<String, String> preferences = new HashMap<>();
        private Integer idApplication;
}
