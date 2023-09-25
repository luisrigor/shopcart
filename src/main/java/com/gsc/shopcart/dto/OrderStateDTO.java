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
public class OrderStateDTO {

        private List<Dealer> vecDealers;
        private Map<String,Dealer> hstDealers;
        private List<Order> vecOrderState;
        private List<OrderStatus> vecOrderStatus;
        private Map<Integer, List<OrderDetail>> hmOrderDetails;
        private Map<Integer,String> suppliers;
        private Map<Integer, String> users;
        private Integer idCatalog;
        private Map<String, String> preferences;}
