package com.gsc.shopcart.service;

import com.gsc.shopcart.dto.GetOrderStateDTO;
import com.gsc.shopcart.dto.OrderStateDTO;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.Order;
import com.gsc.shopcart.model.scart.entity.OrderDetail;
import com.gsc.shopcart.model.scart.entity.OrderStatus;
import com.gsc.shopcart.repository.scart.*;
import com.gsc.shopcart.sample.data.provider.OrderData;
import com.gsc.shopcart.sample.data.provider.SecurityData;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.service.impl.OrderStateServiceImpl;
import com.rg.dealer.Dealer;
import com.rg.dealer.DealerHelper;
import com.sc.commons.exceptions.SCErrorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(SecurityData.ACTIVE_PROFILE)
 class OrderStateServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private OrderStatusRepository orderStatusRepository;
    @Mock
    private DealerHelper dealerHelper;
    @InjectMocks
    private OrderStateServiceImpl orderStateService;

    private SecurityData securityData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityData = new SecurityData();
    }

    @Test
    void whenGetOrderStateThenReturnInfo() throws SCErrorException {
       UserPrincipal user = SecurityData.getUserDefaultStatic();
       GetOrderStateDTO getOrderStateDTO = OrderData.getGetOrderStateDTO();
       user.setOidNet(Dealer.OID_NET_CBUS);

       Vector<Dealer> dealerList =  new Vector<>();
       List<Order> orderList = new ArrayList<>();
       List<OrderStatus> orderStatusList = new ArrayList<>();
       List<OrderDetail> orderDetails = new ArrayList<>();
       Hashtable<String, Dealer> hsmDealers = new Hashtable<>();
       try (MockedStatic<Dealer> utilities = Mockito.mockStatic(Dealer.class)){

          utilities.when(Dealer::getHelper).thenReturn(dealerHelper);
          when(dealerHelper.GetAllActiveMainDealers(anyString())).thenReturn(dealerList);
          when(dealerHelper.getByObjectId(anyString(),anyString())).thenReturn(new Dealer());
          when(dealerHelper.getAllDealers(anyString())).thenReturn(hsmDealers);

          when(orderRepository.getOrderByCriteria(any(),any(),any(),any())).thenReturn(orderList);
          when(orderStatusRepository.findAll()).thenReturn(orderStatusList);
          when(orderDetailRepository.findAll()).thenReturn(orderDetails);

          OrderStateDTO orderStateDTO = orderStateService.getOrderState(user,getOrderStateDTO);

          Assertions.assertEquals(dealerList, orderStateDTO.getDealerList());
          Assertions.assertEquals(orderList, orderStateDTO.getOrderList());
          Assertions.assertEquals(orderStatusList, orderStateDTO.getOrderStatusList());
          Assertions.assertEquals(hsmDealers, orderStateDTO.getHsmDealers());
       }
    }

   @Test
   void whenGetOrderStateThenThrowException() throws SCErrorException {
      UserPrincipal user = SecurityData.getUserDefaultStatic();
      GetOrderStateDTO getOrderStateDTO = OrderData.getGetOrderStateDTO();

      Vector<Dealer> dealerList =  new Vector<>();
      List<Order> orderList = new ArrayList<>();
      List<OrderStatus> orderStatusList = new ArrayList<>();
      List<OrderDetail> orderDetails = new ArrayList<>();
      Hashtable<String, Dealer> hsmDealers = new Hashtable<>();
      try (MockedStatic<Dealer> utilities = Mockito.mockStatic(Dealer.class)){

         utilities.when(Dealer::getHelper).thenThrow(ShopCartException.class);

         assertThrows(ShopCartException.class, ()->orderStateService.getOrderState(user,getOrderStateDTO));

      }
   }
}
