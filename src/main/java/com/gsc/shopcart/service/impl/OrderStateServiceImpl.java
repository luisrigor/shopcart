package com.gsc.shopcart.service.impl;

import com.gsc.shopcart.config.environment.EnvironmentConfig;
import com.gsc.shopcart.config.environment.MapProfileVariables;
import com.gsc.shopcart.constants.ApiConstants;
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
import com.gsc.shopcart.repository.scart.ProductRepository;
import com.gsc.shopcart.repository.usrlogon.*;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.security.UsrLogonSecurity;
import com.gsc.shopcart.service.OrderStateService;
import com.gsc.shopcart.utils.FileShopUtils;
import com.rg.dealer.Dealer;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.utils.StringTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.gsc.shopcart.config.environment.MapProfileVariables.*;

@Service
@Log4j
@RequiredArgsConstructor
public class OrderStateServiceImpl implements OrderStateService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final ToyotaUserRepository toyotaUserRepository;
    private final ToyotaUserEntityProfileRepository toyotaUserEntityProfileRepository;
    private final LexusUserRepository lexusUserRepository;
    private final LexusEntityProfileRepository lexusEntityProfileRepository;
    private final CbusUserRepository cbusUserRepository;
    private final CbusEntityProfileRepository cbusEntityProfileRepository;
    private final ProductRepository productRepository;
    private final UsrLogonSecurity usrLogonSecurity;

    private final EnvironmentConfig environmentConfig;

    @Override
    public OrderStateDTO getOrderState(UserPrincipal userPrincipal, GetOrderStateDTO getOrderStateDTO) {

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

            if (userPrincipal.getAuthorities()==null || userPrincipal.getAuthorities().isEmpty())
                usrLogonSecurity.getAuthorities(userPrincipal);

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
                List<Object[]> listSupps = getSuppliers(userPrincipal.getOidNet(), getOrderStateDTO.getIdProfileTcap(),getOrderStateDTO.getIdProfileSupplier());
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

    private List<Object[]> getSuppliers(String oidNet,Integer idProfileTcap, Integer idProfileSupplier){
        if (oidNet.equalsIgnoreCase(Dealer.OID_NET_TOYOTA)) {
            return toyotaUserEntityProfileRepository.getSuppliers(idProfileTcap, idProfileSupplier);
        }
        else if (oidNet.equalsIgnoreCase(Dealer.OID_NET_LEXUS))
            return lexusEntityProfileRepository.getSuppliers(idProfileTcap,idProfileSupplier);
        else
            return cbusEntityProfileRepository.getSuppliers(idProfileTcap,idProfileSupplier);
    }

    @Override
    public void sendInvoice(UserPrincipal userPrincipal, List<Integer> idOrders) {
        try {
            Map<String, List<Order>> orders = groupOrdersByDealer(idOrders);

            for (Map.Entry<String, List<Order>> entry : orders.entrySet()) {
                Dealer dealer = Dealer.getHelper().getByObjectId(userPrincipal.getOidNet(), entry.getKey());
                String fileName = generateInvoice(dealer, entry.getValue(), userPrincipal.getClientId().intValue());
                updateOrders(entry.getValue(), fileName, userPrincipal);
            }
        } catch (Exception e) {
            throw new ShopCartException("Error Write Files", e);
        }
    }

    public Map<String, List<Order>> groupOrdersByDealer(List<Integer> orderIds) {
        Map<String, List<Order>> result = new HashMap<>();
        for (Integer orderId : orderIds) {
            if (orderId == null || orderId == 0)
                continue;
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new ShopCartException("Order not found by " + orderId));
            result.computeIfAbsent(order.getOidDealer(), key -> new ArrayList<>()).add(order);
        }
        return result;
    }

    public String generateInvoice(Dealer dealer, List<Order> orders, int idApplication) throws SCErrorException {

        String billTo;
        String orderBillTo;
        int orderNumber = orders.get(0).getOrderNumber();

        Map<String, List<OrderDetail>> mapListProductsByOrderAndBillTo = new HashMap<>();
        Map<String, String> envV = environmentConfig.getEnvVariables();

        for(Order oOrder : orders){//percorre a lista de orders recebidas
            List<OrderDetail> orderDetailList = orderDetailRepository.findByIdOrderAndIdOrderStatus(oOrder.getId(),ScConstants.ID_ORDER_STATUS_DELIVERED);//obtem o vetor de produtos da encomenda
            for(OrderDetail orderDetail : orderDetailList){
                billTo= productRepository.getBillToByIdProduct(orderDetail.getIdProduct());
                if (billTo == null || billTo.trim().equals(StringUtils.EMPTY)) {
                    if (idApplication == ApiConstants.TOYOTA_APP)
                        billTo = "0238";
                    else if(idApplication == ApiConstants.LEXUS_APP)
                        billTo = "0288";
                }
                orderBillTo = oOrder.getOrderNumber()+billTo;
                mapListProductsByOrderAndBillTo.computeIfAbsent(orderBillTo, key -> new ArrayList<>()).add(orderDetail);
            }
        }
        return FileShopUtils.setFiles(mapListProductsByOrderAndBillTo,idApplication,orderNumber,dealer,envV.get(PATH_TO_WRITE_FILES));
    }

    private void updateOrders(List<Order> orders, String fileName, UserPrincipal user) {
        String changedBy = user.getUsername();
        for (Order order : orders) {
            orderRepository.updateAlData(fileName, LocalDateTime.now(),changedBy,LocalDateTime.now(),order.getId());
        }
    }

    public void listOrderDetail(UserPrincipal user, Integer idOrder, Integer idOrderDetailStatus){

        idOrder=idOrder==null?0:idOrder;
        idOrderDetailStatus=idOrderDetailStatus==null?-1:idOrder;

        String criteria = "1=1";

        List<OrderDetail> orderDetailList = new ArrayList<>();
        List<OrderStatus> orderStatusList = new ArrayList<>();

        Map suppliers = new HashMap();

        Order order = null;

        /**Confirmar si estos valores ya los puedo extraer del user*/
        String idProfileTcap = user.getTcapProfile();
        String idProfileDealer = user.getDealerProfile();
        String idProfileSupplier = user.getSupplierProfile();


        int idCatalog = Integer.parseInt(request.getPreferences().getValue("idCatalog", "0"));
        int idApplication = StringTasks.cleanInteger(request.getPreferences().getValue("idApplication", "0"),0);

        Map preferences = new HashMap();
        preferences.put("idOrderStatus", String.valueOf(StringTasks.cleanInteger(request.getParameter("idOrderStatus"), -1)));
        preferences.put("idSupplier", String.valueOf(StringTasks.cleanInteger(request.getParameter("idSupplier"), -1)));
        preferences.put("oidDealer", StringTasks.cleanString(request.getParameter("oidDealer"), ""));
        preferences.put("orderType", StringTasks.cleanString(request.getParameter("orderType"), ORDER_TYPE_EXTRANET));
        preferences.put(ApplicationConfiguration.CONFIGURATION_ID_APPLICATION, idApplication);
        preferences.put(ApplicationConfiguration.CONFIGURATION_ID_PROFILE_TCAP, idProfileTcap);
        preferences.put(ApplicationConfiguration.CONFIGURATION_ID_PROFILE_DEALER, idProfileDealer);
        preferences.put(ApplicationConfiguration.CONFIGURATION_ID_PROFILE_SUPPLIER, idProfileSupplier);
        request.setAttribute("preferences", preferences);

        try {

            order = (Order) Order.getHelper().getObjectById(idOrder, ApplicationConfiguration.DATASOURCE_DBSHOPCART);
            if(idOrderDetailStatus!=-1)
                criteria+= " AND ID_ORDER_STATUS = "+idOrderDetailStatus+" ";

            if (userbean.getAuthorities().contains(ApplicationConfiguration.PROFILE_SUPPLIER) && !userbean.getAuthorities().contains(ApplicationConfiguration.PROFILE_TCAP))
                criteria+= " AND ID_SUPPLIER = "+userbean.getIdEntity()+" ";

            orderDetailList = OrderDetail.getHelper().getOrderDetailByIdOrder(idOrder,criteria);
            orderStatusList = OrderStatus.getHelper().getOrderStatus();
            suppliers = ShopCartUtils.getSuppliers(Integer.parseInt(idProfileTcap), Integer.parseInt(idProfileSupplier),userbean.getOidNet());

        } catch (SCErrorException e) {
            StringBuffer errorMsg = new StringBuffer("");
            errorMsg.append("Message:" + e.getMessage() + "\n");
            errorMsg.append("LocalizedMessage:" + e.getLocalizedMessage() + "\n");
            errorMsg.append("ExceptionStackTrace:\n" + e.getExceptionStackTrace() + "\n");
            logger.error(errorMsg.toString());
            msg = e.getLocalizedMessage();
        }

        request.setAttribute("msg", msg);
        request.setAttribute("orderDetailList", orderDetailList);
        request.setAttribute("orderStatusList", orderStatusList);
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("order", order);
        request.setAttribute("idCatalog", idCatalog);
        request.setAttribute("idApplication", String.valueOf(idApplication));


    }

    /*private void ListOrderDetail(RenderRequest request, User userbean) {

		int idOrder = StringTasks.cleanInteger(request.getParameter("idOrder"), 0);
		int idOrderDetailStatus = StringTasks.cleanInteger(request.getParameter("idOrderDetailStatus"), -1);
		String criteria = "1=1";
		String msg = PortletTasks.getMessage(request);
		Vector vecOrderDetail = new Vector();
		Vector vecOrderStatus = new Vector();
		Hashtable suppliers = new Hashtable();
		Order order = null;


		String idProfileTcap = request.getPreferences().getValue(ApplicationConfiguration.CONFIGURATION_ID_PROFILE_TCAP, "0");
		String idProfileDealer = request.getPreferences().getValue(ApplicationConfiguration.CONFIGURATION_ID_PROFILE_DEALER, "0");
		String idProfileSupplier = request.getPreferences().getValue(ApplicationConfiguration.CONFIGURATION_ID_PROFILE_SUPPLIER, "0");

		int idCatalog = Integer.parseInt(request.getPreferences().getValue("idCatalog", "0"));
		int idApplication = StringTasks.cleanInteger(request.getPreferences().getValue("idApplication", "0"),0);

		Map preferences = new HashMap();
		preferences.put("idOrderStatus", String.valueOf(StringTasks.cleanInteger(request.getParameter("idOrderStatus"), -1)));
		preferences.put("idSupplier", String.valueOf(StringTasks.cleanInteger(request.getParameter("idSupplier"), -1)));
		preferences.put("oidDealer", StringTasks.cleanString(request.getParameter("oidDealer"), ""));
		preferences.put("orderType", StringTasks.cleanString(request.getParameter("orderType"), ORDER_TYPE_EXTRANET));
		preferences.put(ApplicationConfiguration.CONFIGURATION_ID_APPLICATION, idApplication);
		preferences.put(ApplicationConfiguration.CONFIGURATION_ID_PROFILE_TCAP, idProfileTcap);
		preferences.put(ApplicationConfiguration.CONFIGURATION_ID_PROFILE_DEALER, idProfileDealer);
		preferences.put(ApplicationConfiguration.CONFIGURATION_ID_PROFILE_SUPPLIER, idProfileSupplier);
		request.setAttribute("preferences", preferences);

		try {

			order = (Order) Order.getHelper().getObjectById(idOrder, ApplicationConfiguration.DATASOURCE_DBSHOPCART);
			if(idOrderDetailStatus!=-1)
				criteria+= " AND ID_ORDER_STATUS = "+idOrderDetailStatus+" ";

			if (userbean.getAuthorities().contains(ApplicationConfiguration.PROFILE_SUPPLIER) && !userbean.getAuthorities().contains(ApplicationConfiguration.PROFILE_TCAP))
				criteria+= " AND ID_SUPPLIER = "+userbean.getIdEntity()+" ";

			vecOrderDetail = OrderDetail.getHelper().getOrderDetailByIdOrder(idOrder,criteria);
			vecOrderStatus = OrderStatus.getHelper().getOrderStatus();
			suppliers = ShopCartUtils.getSuppliers(Integer.parseInt(idProfileTcap), Integer.parseInt(idProfileSupplier),userbean.getOidNet());

		} catch (SCErrorException e) {
			StringBuffer errorMsg = new StringBuffer("");
			errorMsg.append("Message:" + e.getMessage() + "\n");
			errorMsg.append("LocalizedMessage:" + e.getLocalizedMessage() + "\n");
			errorMsg.append("ExceptionStackTrace:\n" + e.getExceptionStackTrace() + "\n");
			logger.error(errorMsg.toString());
			msg = e.getLocalizedMessage();
		}

		request.setAttribute("msg", msg);
		request.setAttribute("vecOrderDetail", vecOrderDetail);
		request.setAttribute("vecOrderStatus", vecOrderStatus);
		request.setAttribute("suppliers", suppliers);
		request.setAttribute("order", order);
		request.setAttribute("idCatalog", idCatalog);
		request.setAttribute("idApplication", String.valueOf(idApplication));
	}

     */
}
