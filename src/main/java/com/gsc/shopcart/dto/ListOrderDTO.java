package com.gsc.shopcart.dto;

import com.gsc.shopcart.model.scart.entity.Order;
import com.gsc.shopcart.model.scart.entity.OrderDetail;
import com.gsc.shopcart.model.scart.entity.OrderStatus;
import com.sc.commons.utils.StringTasks;
import lombok.*;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListOrderDTO {

    private Order order;
    private List<OrderDetail> orderDetailList;
    private List<OrderStatus> orderStatusList;
    private Map<Integer, String> suppliers;

}
