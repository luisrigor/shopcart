package com.gsc.shopcart.service;

import com.gsc.shopcart.constants.ScConstants;
import com.gsc.shopcart.dto.GetOrderStateDTO;
import com.gsc.shopcart.dto.OrderStateDTO;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.Order;
import com.gsc.shopcart.model.scart.entity.OrderDetail;
import com.gsc.shopcart.model.scart.entity.OrderStatus;
import com.gsc.shopcart.repository.scart.*;
import com.gsc.shopcart.repository.usrlogon.*;
import com.gsc.shopcart.sample.data.provider.OrderData;
import com.gsc.shopcart.sample.data.provider.SecurityData;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.security.UsrLogonSecurity;
import com.gsc.shopcart.service.impl.OrderStateServiceImpl;
import com.gsc.shopcart.utils.FileShopUtils;
import com.rg.dealer.Dealer;
import com.rg.dealer.DealerHelper;
import com.sc.commons.exceptions.SCErrorException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ActiveProfiles(SecurityData.ACTIVE_PROFILE)
 class OrderStateServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private OrderStatusRepository orderStatusRepository;
    @Mock
    private ToyotaUserRepository toyotaUserRepository;
    @Mock
    private ToyotaUserEntityProfileRepository toyotaUserEntityProfileRepository;
    @Mock
    private LexusUserRepository lexusUserRepository;
    @Mock
    private LexusEntityProfileRepository lexusEntityProfileRepository;
    @Mock
    private CbusUserRepository cbusUserRepository;
    @Mock
    private CbusEntityProfileRepository cbusEntityProfileRepository;
    @Mock
    private UsrLogonSecurity usrLogonSecurity;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private DealerHelper dealerHelper;
    @Mock
    private FileShopUtils fileShopUtils;
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

       user.setOidNet(Dealer.OID_NET_TOYOTA);
       user.setAuthorities(new ArrayList<>(Collections.singletonList(ScConstants.PROFILE_TCAP)));
       Vector<Dealer> dealerList =  new Vector<>();
       List<Order> orderList = new ArrayList<>();
       List<OrderStatus> orderStatusList = new ArrayList<>();
       List<OrderDetail> orderDetails = new ArrayList<>();
       List<Object[]> supplierList = new ArrayList<>();
       List<Object[]> userList = new ArrayList<>();
       userList.add(new Object[] { 1, "oidDealerParent1" });
       Hashtable<String, Dealer> hsmDealers = new Hashtable<>();


       try (MockedStatic<Dealer> utilities = Mockito.mockStatic(Dealer.class)){

          utilities.when(Dealer::getHelper).thenReturn(dealerHelper);
          when(dealerHelper.GetAllActiveMainDealers(anyString())).thenReturn(dealerList);
          when(dealerHelper.getByObjectId(anyString(),anyString())).thenReturn(new Dealer());
          when(dealerHelper.getAllDealers(anyString())).thenReturn(hsmDealers);
          when(toyotaUserEntityProfileRepository.getSuppliers(anyInt(),anyInt())).thenReturn(supplierList);
          when(toyotaUserRepository.getIdAndName(any())).thenReturn(userList);

          when(orderRepository.getOrderByCriteria(any(),any(),any(),any())).thenReturn(orderList);
          when(orderStatusRepository.findAll()).thenReturn(orderStatusList);
          when(orderDetailRepository.findAll()).thenReturn(orderDetails);

          OrderStateDTO orderStateDTO = orderStateService.getOrderState(user,getOrderStateDTO);

          assertEquals(dealerList, orderStateDTO.getDealerList());
          assertEquals(orderList, orderStateDTO.getOrderList());
          assertEquals(orderStatusList, orderStateDTO.getOrderStatusList());
          assertEquals(hsmDealers, orderStateDTO.getHsmDealers());
       }
    }

   @Test
   void whenGetOrderStateAndOidParentIsEmptyThenReturnInfo() throws SCErrorException {
      UserPrincipal user = SecurityData.getUserDefaultStatic();
      GetOrderStateDTO getOrderStateDTO = OrderData.getGetOrderStateDTO();
      getOrderStateDTO.setOidParent("");

      user.setOidNet(Dealer.OID_NET_TOYOTA);
      user.setAuthorities(new ArrayList<>(Collections.singletonList(ScConstants.PROFILE_TCAP)));
      Vector<Dealer> dealerList =  new Vector<>();
      List<Order> orderList = new ArrayList<>();
      List<OrderStatus> orderStatusList = new ArrayList<>();
      List<OrderDetail> orderDetails = new ArrayList<>();
      List<Object[]> supplierList = new ArrayList<>();
      List<Object[]> userList = new ArrayList<>();
      userList.add(new Object[] { 1, "oidDealerParent1" });
      Hashtable<String, Dealer> hsmDealers = new Hashtable<>();


      try (MockedStatic<Dealer> utilities = Mockito.mockStatic(Dealer.class)){

         utilities.when(Dealer::getHelper).thenReturn(dealerHelper);
         when(dealerHelper.GetAllActiveMainDealers(anyString())).thenReturn(dealerList);
         when(dealerHelper.getByObjectId(anyString(),anyString())).thenReturn(new Dealer());
         when(dealerHelper.getAllDealers(anyString())).thenReturn(hsmDealers);
         when(toyotaUserEntityProfileRepository.getSuppliers(anyInt(),anyInt())).thenReturn(supplierList);
         when(toyotaUserRepository.getIdAndName()).thenReturn(userList);

         when(orderRepository.getOrderByCriteria(any(),any(),any(),any())).thenReturn(orderList);
         when(orderStatusRepository.findAll()).thenReturn(orderStatusList);
         when(orderDetailRepository.findAll()).thenReturn(orderDetails);

         OrderStateDTO orderStateDTO = orderStateService.getOrderState(user,getOrderStateDTO);

         assertEquals(dealerList, orderStateDTO.getDealerList());
         assertEquals(orderList, orderStateDTO.getOrderList());
         assertEquals(orderStatusList, orderStateDTO.getOrderStatusList());
         assertEquals(hsmDealers, orderStateDTO.getHsmDealers());
      }
   }

   @Test
   void whenGetOrderStateWhenOidNetIsCbusThenReturnInfo() throws SCErrorException {
      UserPrincipal user = SecurityData.getUserDefaultStatic();
      GetOrderStateDTO getOrderStateDTO = OrderData.getGetOrderStateDTO();

      user.setOidNet(Dealer.OID_NET_CBUS);
      user.setAuthorities(new ArrayList<>(Collections.singletonList(ScConstants.PROFILE_TCAP)));
      Vector<Dealer> dealerList =  new Vector<>();
      List<Order> orderList = new ArrayList<>();
      List<OrderStatus> orderStatusList = new ArrayList<>();
      List<OrderDetail> orderDetails = new ArrayList<>();
      List<Object[]> supplierList = new ArrayList<>();
      List<Object[]> userList = new ArrayList<>();
      userList.add(new Object[] { 1, "oidDealerParent1" });
      Hashtable<String, Dealer> hsmDealers = new Hashtable<>();


      try (MockedStatic<Dealer> utilities = Mockito.mockStatic(Dealer.class)){

         utilities.when(Dealer::getHelper).thenReturn(dealerHelper);
         when(dealerHelper.GetAllActiveMainDealers(anyString())).thenReturn(dealerList);
         when(dealerHelper.getByObjectId(anyString(),anyString())).thenReturn(new Dealer());
         when(dealerHelper.getAllDealers(anyString())).thenReturn(hsmDealers);
         when(cbusEntityProfileRepository.getSuppliers(anyInt(),anyInt())).thenReturn(supplierList);
         when(cbusUserRepository.getIdAndName(any())).thenReturn(userList);

         when(orderRepository.getOrderByCriteria(any(),any(),any(),any())).thenReturn(orderList);
         when(orderStatusRepository.findAll()).thenReturn(orderStatusList);
         when(orderDetailRepository.findAll()).thenReturn(orderDetails);

         OrderStateDTO orderStateDTO = orderStateService.getOrderState(user,getOrderStateDTO);

         assertEquals(dealerList, orderStateDTO.getDealerList());
         assertEquals(orderList, orderStateDTO.getOrderList());
         assertEquals(orderStatusList, orderStateDTO.getOrderStatusList());
         assertEquals(hsmDealers, orderStateDTO.getHsmDealers());
      }
   }

   @Test
   void whenGetOrderStateWhenOidNetIsCbusAndOidParentIsEmptyThenReturnInfo() throws SCErrorException {
      UserPrincipal user = SecurityData.getUserDefaultStatic();
      GetOrderStateDTO getOrderStateDTO = OrderData.getGetOrderStateDTO();
      getOrderStateDTO.setOidParent("");

      user.setOidNet(Dealer.OID_NET_CBUS);
      user.setAuthorities(new ArrayList<>(Collections.singletonList(ScConstants.PROFILE_TCAP)));
      Vector<Dealer> dealerList =  new Vector<>();
      List<Order> orderList = new ArrayList<>();
      List<OrderStatus> orderStatusList = new ArrayList<>();
      List<OrderDetail> orderDetails = new ArrayList<>();
      List<Object[]> supplierList = new ArrayList<>();
      List<Object[]> userList = new ArrayList<>();
      userList.add(new Object[] { 1, "oidDealerParent1" });
      Hashtable<String, Dealer> hsmDealers = new Hashtable<>();


      try (MockedStatic<Dealer> utilities = Mockito.mockStatic(Dealer.class)){

         utilities.when(Dealer::getHelper).thenReturn(dealerHelper);
         when(dealerHelper.GetAllActiveMainDealers(anyString())).thenReturn(dealerList);
         when(dealerHelper.getByObjectId(anyString(),anyString())).thenReturn(new Dealer());
         when(dealerHelper.getAllDealers(anyString())).thenReturn(hsmDealers);
         when(cbusEntityProfileRepository.getSuppliers(anyInt(),anyInt())).thenReturn(supplierList);
         when(cbusUserRepository.getIdAndName()).thenReturn(userList);

         when(orderRepository.getOrderByCriteria(any(),any(),any(),any())).thenReturn(orderList);
         when(orderStatusRepository.findAll()).thenReturn(orderStatusList);
         when(orderDetailRepository.findAll()).thenReturn(orderDetails);

         OrderStateDTO orderStateDTO = orderStateService.getOrderState(user,getOrderStateDTO);

         assertEquals(dealerList, orderStateDTO.getDealerList());
         assertEquals(orderList, orderStateDTO.getOrderList());
         assertEquals(orderStatusList, orderStateDTO.getOrderStatusList());
         assertEquals(hsmDealers, orderStateDTO.getHsmDealers());
      }
   }


   @Test
   void whenGetOrderStateWhenOidNetIsLexusThenReturnInfo() throws SCErrorException {
      UserPrincipal user = SecurityData.getUserDefaultStatic();
      GetOrderStateDTO getOrderStateDTO = OrderData.getGetOrderStateDTO();

      user.setOidNet(Dealer.OID_NET_LEXUS);
      user.setAuthorities(new ArrayList<>(Collections.singletonList(ScConstants.PROFILE_TCAP)));
      Vector<Dealer> dealerList =  new Vector<>();
      List<Order> orderList = new ArrayList<>();
      List<OrderStatus> orderStatusList = new ArrayList<>();
      List<OrderDetail> orderDetails = new ArrayList<>();
      List<Object[]> supplierList = new ArrayList<>();
      List<Object[]> userList = new ArrayList<>();
      userList.add(new Object[] { 1, "oidDealerParent1" });
      Hashtable<String, Dealer> hsmDealers = new Hashtable<>();


      try (MockedStatic<Dealer> utilities = Mockito.mockStatic(Dealer.class)){

         utilities.when(Dealer::getHelper).thenReturn(dealerHelper);
         when(dealerHelper.GetAllActiveMainDealers(anyString())).thenReturn(dealerList);
         when(dealerHelper.getByObjectId(anyString(),anyString())).thenReturn(new Dealer());
         when(dealerHelper.getAllDealers(anyString())).thenReturn(hsmDealers);
         when(lexusEntityProfileRepository.getSuppliers(anyInt(),anyInt())).thenReturn(supplierList);
         when(lexusUserRepository.getIdAndName(any())).thenReturn(userList);

         when(orderRepository.getOrderByCriteria(any(),any(),any(),any())).thenReturn(orderList);
         when(orderStatusRepository.findAll()).thenReturn(orderStatusList);
         when(orderDetailRepository.findAll()).thenReturn(orderDetails);

         OrderStateDTO orderStateDTO = orderStateService.getOrderState(user,getOrderStateDTO);

         assertEquals(dealerList, orderStateDTO.getDealerList());
         assertEquals(orderList, orderStateDTO.getOrderList());
         assertEquals(orderStatusList, orderStateDTO.getOrderStatusList());
         assertEquals(hsmDealers, orderStateDTO.getHsmDealers());
      }
   }

   @Test
   void whenGetOrderStateWhenOidNetIsLexusAndOidParentIsEmptyThenReturnInfo() throws SCErrorException {
      UserPrincipal user = SecurityData.getUserDefaultStatic();
      GetOrderStateDTO getOrderStateDTO = OrderData.getGetOrderStateDTO();
      getOrderStateDTO.setOidParent("");

      user.setOidNet(Dealer.OID_NET_LEXUS);
      user.setAuthorities(new ArrayList<>(Collections.singletonList(ScConstants.PROFILE_TCAP)));
      Vector<Dealer> dealerList =  new Vector<>();
      List<Order> orderList = new ArrayList<>();
      List<OrderStatus> orderStatusList = new ArrayList<>();
      List<OrderDetail> orderDetails = new ArrayList<>();
      List<Object[]> supplierList = new ArrayList<>();
      List<Object[]> userList = new ArrayList<>();
      userList.add(new Object[] { 1, "oidDealerParent1" });
      Hashtable<String, Dealer> hsmDealers = new Hashtable<>();

      try (MockedStatic<Dealer> utilities = Mockito.mockStatic(Dealer.class)){

         utilities.when(Dealer::getHelper).thenReturn(dealerHelper);
         when(dealerHelper.GetAllActiveMainDealers(anyString())).thenReturn(dealerList);
         when(dealerHelper.getByObjectId(anyString(),anyString())).thenReturn(new Dealer());
         when(dealerHelper.getAllDealers(anyString())).thenReturn(hsmDealers);
         when(lexusEntityProfileRepository.getSuppliers(anyInt(),anyInt())).thenReturn(supplierList);
         when(lexusUserRepository.getIdAndName()).thenReturn(userList);

         when(orderRepository.getOrderByCriteria(any(),any(),any(),any())).thenReturn(orderList);
         when(orderStatusRepository.findAll()).thenReturn(orderStatusList);
         when(orderDetailRepository.findAll()).thenReturn(orderDetails);

         OrderStateDTO orderStateDTO = orderStateService.getOrderState(user,getOrderStateDTO);

         assertEquals(dealerList, orderStateDTO.getDealerList());
         assertEquals(orderList, orderStateDTO.getOrderList());
         assertEquals(orderStatusList, orderStateDTO.getOrderStatusList());
         assertEquals(hsmDealers, orderStateDTO.getHsmDealers());
      }
   }

   @Test
   void whenGetOrderStateThenThrowException() {
      UserPrincipal user = SecurityData.getUserDefaultStatic();
      GetOrderStateDTO getOrderStateDTO = OrderData.getGetOrderStateDTO();
      user.setAuthorities(null);
      doNothing().when(usrLogonSecurity).getAuthorities(any());

      try (MockedStatic<Dealer> utilities = Mockito.mockStatic(Dealer.class)){

         utilities.when(Dealer::getHelper).thenThrow(ShopCartException.class);
         assertThrows(ShopCartException.class, ()->orderStateService.getOrderState(user,getOrderStateDTO));
      }
   }

   @Test
   void generateInvoiceWithSuccessfullyCase() throws SCErrorException {
      Order order = OrderData.getOrderBuilder();
      Dealer dealer = new Dealer();
      OrderDetail orderDetail = OrderData.getOrderDetailBuilder();
      String billTo = StringUtils.EMPTY;
      when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));
      String expectedFileName = "FileName";
      Map<String, List<Order>> orders = new HashMap<>();
      orders.put(order.getOidDealer(),Collections.singletonList(order));

      try (MockedStatic<FileShopUtils> fileShopUtils = Mockito.mockStatic(FileShopUtils.class)) {

         when(dealerHelper.getByObjectId(anyString(),anyString())).thenReturn(dealer);
         when(orderDetailRepository.findByIdOrderAndIdOrderStatus(anyInt(),anyInt())).thenReturn(Collections.singletonList(orderDetail));
         when(productRepository.getBillToByIdProduct(anyInt())).thenReturn(billTo);
         fileShopUtils.when(() -> FileShopUtils.setFiles(anyMap(),anyInt(),anyInt(),any()))
                 .thenReturn(expectedFileName);
         String fileName = orderStateService.generateInvoice(dealer,orders.get(order.getOidDealer()));
         assertEquals(expectedFileName,fileName);
      }
   }

   @Test
   void sendInvoiceWithSuccessfullyCase() throws SCErrorException {
      UserPrincipal user = SecurityData.getUserDefaultStatic();
      Order order = OrderData.getOrderBuilder();
      Dealer dealer = new Dealer();
      OrderDetail orderDetail = OrderData.getOrderDetailBuilder();
      String billTo = StringUtils.EMPTY;
      when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));
      String expectedFileName = "FileName";
      Map<String, List<Order>> orders = new HashMap<>();
      orders.put(order.getOidDealer(),Collections.singletonList(order));
      try (MockedStatic<Dealer> delUtils = Mockito.mockStatic(Dealer.class);
           MockedStatic<FileShopUtils> fileShopUtils = Mockito.mockStatic(FileShopUtils.class)) {
         delUtils.when(Dealer::getHelper).thenReturn(dealerHelper);
         when(dealerHelper.getByObjectId(anyString(),anyString())).thenReturn(dealer);
         when(orderDetailRepository.findByIdOrderAndIdOrderStatus(anyInt(),anyInt())).thenReturn(Collections.singletonList(orderDetail));
         when(productRepository.getBillToByIdProduct(anyInt())).thenReturn(billTo);
         fileShopUtils.when(() -> FileShopUtils.setFiles(anyMap(),anyInt(),anyInt(),any()))
                 .thenReturn(expectedFileName);
         orderStateService.sendInvoice(user,new ArrayList<>(Arrays.asList(1,0)));
         verify(productRepository).getBillToByIdProduct(anyInt());
         verify(orderRepository,times(1)).updateAlData(any(),any(),anyString(),any(),anyInt());
      }
   }

   @Test
   void sendInvoiceThenThrowShopCarException() {
      UserPrincipal user = SecurityData.getUserDefaultStatic();
      when(orderRepository.findById(anyInt())).thenThrow(ShopCartException.class);
      assertThrows(ShopCartException.class,()->
              orderStateService.sendInvoice(user,new ArrayList<>(Arrays.asList(1,0))));

   }


}
