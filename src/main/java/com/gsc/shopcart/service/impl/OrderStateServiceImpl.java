package com.gsc.shopcart.service.impl;

import com.gsc.shopcart.constants.ScConstants;
import com.gsc.shopcart.dto.GetOrderStateDTO;
import com.gsc.shopcart.dto.OrderStateDTO;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.Order;
import com.gsc.shopcart.model.scart.entity.OrderDetail;
import com.gsc.shopcart.model.scart.entity.OrderStatus;
import com.gsc.shopcart.repository.scart.OrderDetailRepository;
import com.gsc.shopcart.repository.scart.OrderRepository;
import com.gsc.shopcart.repository.scart.OrderStatusRepository;
import com.gsc.shopcart.repository.usrlogon.*;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.security.UsrLogonSecurity;
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
    private final ToyotaUserRepository toyotaUserRepository;
    private final ToyotaUserEntityProfileRepository toyotaUserEntityProfileRepository;
    private final LexusUserRepository lexusUserRepository;
    private final LexusEntityProfileRepository lexusEntityProfileRepository;
    private final CbusUserRepository cbusUserRepository;
    private final CbusEntityProfileRepository cbusEntityProfileRepository;
    private final UsrLogonSecurity usrLogonSecurity;

    @Override
    public OrderStateDTO getOrderState(UserPrincipal userPrincipal, GetOrderStateDTO getOrderStateDTO) {

        try {
            if (userPrincipal.getIdUser() == null || userPrincipal.getIdUser() == -1)
                usrLogonSecurity.setUserLogin(userPrincipal);

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

            Map<Integer, String> suppliers = new HashMap<>();
            if (userPrincipal.getAuthorities().contains(ScConstants.PROFILE_TCAP) || userPrincipal.getAuthorities().contains(ScConstants.PROFILE_DEALER)) {
                List<Object[]> listSupps = getSuppliers(userPrincipal.getOidNet(), getOrderStateDTO.getIdProfileTcap(),getOrderStateDTO.getIdSupplier());
                suppliers = setMapData(listSupps);
            }

            List<Object[]> listUser = getUsers(userPrincipal.getOidNet(), oidParent);
            Map<Integer, String> users =  setMapData(listUser);
            Map<Integer, List<OrderDetail>> hsmOrderDetails = orderDetailRepository.findAll().stream()
                    .collect(Collectors.groupingBy(OrderDetail::getIdOrder));

            return OrderStateDTO.builder()
                    .dealerList(dealerList)
                    .hsmDealers(hsmDealers)
                    .orderList(orderList)
                    .orderStatusList(orderStatusList)
                    .hsmOrderDetails(hsmOrderDetails)
                    .suppliers(suppliers)
                    .users(users)
                    .idCatalog(getOrderStateDTO.getIdCatalog())
                    .preferences(preferences)
                    .idApplication(getOrderStateDTO.getIdApplication())
                    .build();
        } catch (Exception e) {
            throw new ShopCartException("Error fetching order status ", e);
        }
    }

    private Map<Integer, String> setMapData(List<Object[]> data) {
        HashMap<Integer, String> mapUsers = new HashMap<>();
        for (Object[] currentRow: data) {
            mapUsers.put((Integer) currentRow[0], (String) currentRow[1]);
        }
        return mapUsers;
    }

    private List<Object[]> getUsers(String oidNet, String oidDealerParent){
        if (oidDealerParent.isEmpty()) {
            if (oidNet.equalsIgnoreCase(Dealer.OID_NET_TOYOTA))
                return toyotaUserRepository.getIdAndName();
            else if (oidNet.equalsIgnoreCase(Dealer.OID_NET_LEXUS))
                return lexusUserRepository.getIdAndName();
            else
                return cbusUserRepository.getIdAndName();
        }
        if (oidNet.equalsIgnoreCase(Dealer.OID_NET_TOYOTA))
            return toyotaUserRepository.getIdAndName(oidDealerParent);
        else if (oidNet.equalsIgnoreCase(Dealer.OID_NET_LEXUS))
            return lexusUserRepository.getIdAndName(oidDealerParent);
        else
            return cbusUserRepository.getIdAndName(oidDealerParent);
    }

    private List<Object[]> getSuppliers(String oidNet,Integer idProfileTcap, Integer idSupplier){
        if (oidNet.equalsIgnoreCase(Dealer.OID_NET_TOYOTA))
            return toyotaUserEntityProfileRepository.getSuppliers(idProfileTcap,idSupplier);
        else if (oidNet.equalsIgnoreCase(Dealer.OID_NET_LEXUS))
            return lexusEntityProfileRepository.getSuppliers(idProfileTcap,idSupplier);
        else
            return cbusEntityProfileRepository.getSuppliers(idProfileTcap,idSupplier);
    }

}
