package com.gsc.shopcart.service.impl;

import com.gsc.shopcart.constants.ScConstants;
import com.gsc.shopcart.dto.GetOrderStateDTO;
import com.gsc.shopcart.dto.OrderStatusDTO;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.Order;
import com.gsc.shopcart.model.scart.entity.OrderDetail;
import com.gsc.shopcart.model.scart.entity.OrderStatus;
import com.gsc.shopcart.repository.scart.OrderDetailRepository;
import com.gsc.shopcart.repository.scart.OrderRepository;
import com.gsc.shopcart.repository.scart.OrderStatusRepository;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.service.OrderStatusService;
import com.rg.dealer.Dealer;
import com.sc.commons.utils.StringTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j
@RequiredArgsConstructor
public class OrderStateServiceImpl  implements OrderStatusService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderStatusRepository orderStatusRepository;

    @Override
    public OrderStatusDTO getOrderState(UserPrincipal userPrincipal, GetOrderStateDTO getOrderStateDTO) {

        try {

            String oidParent = StringTasks.cleanString(getOrderStateDTO.getOidParent(),StringUtils.EMPTY);
            StringBuilder criteria = new StringBuilder(" 1=1 ");
            StringBuilder criteriaDetail = new StringBuilder(" 1=1 ");

            Map<String, String> preferences = new HashMap<>();
            preferences.put("idOrderStatus", String.valueOf(getOrderStateDTO.getIdOrderStatus()));
            preferences.put("idSupplier", String.valueOf(getOrderStateDTO.getIdSupplier()));
            preferences.put("idUser", String.valueOf(getOrderStateDTO.getIdUser()));
            preferences.put("orderNr", String.valueOf(getOrderStateDTO.getOrderNr()));
            preferences.put("iPec", getOrderStateDTO.getIPec());
            preferences.put("oidDealer", oidParent);
            preferences.put("orderType", StringTasks.cleanString(getOrderStateDTO.getOrderType(), ScConstants.ORDER_TYPE_EXTRANET));
            preferences.put("reference", getOrderStateDTO.getReference());

            List<Dealer> dealerList = Dealer.getHelper().GetAllActiveMainDealers(userPrincipal.getOidNet());
            dealerList.add(Dealer.getHelper().getByObjectId(userPrincipal.getOidNet(), Dealer.OID_NMSC));

            Map<String,Dealer> hsmDealers = Dealer.getHelper().getAllDealers(userPrincipal.getOidNet());
            hsmDealers.put(Dealer.OID_NMSC, Dealer.getHelper().getByObjectId(userPrincipal.getOidNet(), Dealer.OID_NMSC));
            if(userPrincipal.getOidNet().equals(Dealer.OID_NET_CBUS))
                hsmDealers.put(Dealer.OID_CBUS, Dealer.getHelper().getByObjectId(userPrincipal.getOidNet(), Dealer.OID_CBUS));

            List<Order> orderList = orderRepository.getOrderByCriteria(getOrderStateDTO,userPrincipal,criteria,criteriaDetail);
            List<OrderStatus> orderStatusList = orderStatusRepository.findAll();

            Map<String,String> suppliers = new HashMap<>();
/*            if (userPrincipal.getAuthorities().contains(ScConstants.PROFILE_TCAP) || userPrincipal.getAuthorities().contains(ScConstants.PROFILE_DEALER))
                suppliers = orderRepository.getSuppliers(getOrderStateDTO.getIdProfileTcap(), getOrderStateDTO.getIdSupplier(), userPrincipal.getOidNet());
 */
           // LinkedHashMap<String, String> users = orderRepository.getUsersByApplication(userPrincipal.getOidNet(), oidParent);
            Map<Integer, List<OrderDetail>> hsmOrderDetails = orderDetailRepository.findAll().stream()
                    .collect(Collectors.groupingBy(OrderDetail::getIdOrder));

            return OrderStatusDTO.builder()
                    .dealerList(dealerList)
                    .hsmDealers(hsmDealers)
                    .orderList(orderList)
                    .orderStatusList(orderStatusList)
                    .hsmOrderDetails(hsmOrderDetails)
                    .suppliers(suppliers)
                    .users(new LinkedHashMap<>())
                    .idCatalog(getOrderStateDTO.getIdCatalog())
                    .preferences(preferences)
                    .idApplication(getOrderStateDTO.getIdApplication())
                    .build();
        } catch (Exception e) {
            throw new ShopCartException("Error fetching order status ", e);
        }
    }

}
