package com.gsc.shopcart.service.impl;

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
import com.rg.dealer.Dealer;
import com.sc.commons.utils.DateTimerTasks;
import com.sc.commons.utils.StringTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j
@RequiredArgsConstructor
public class OrderStateServiceImpl  implements OrderStateService {

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
    public Map sendInvoice(UserPrincipal userPrincipal, List<Integer> idOrders) {
        Map<String, List<Order>> orders = groupOrdersByDealer(idOrders);

        return orders;
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

    public  String generateInvoice(Dealer dealer, List<Order> orders, UserPrincipal user, int idApplication) {
        int year = DateTimerTasks.getCurYear();
        String registryKeyPrefix = "CRCMP";
        String program;
        double price;
        int quantity;
        String fileName=StringUtils.EMPTY;
        String fileNameFinal=StringUtils.EMPTY;

        if (idApplication==10008) program = "CARRCMP-02";
        else program = "CARRCMP-01";

        String costCenter = "";// o mesmo que billTo na base de dados
        String invoiceOperation = "00069";
        String movementType = "D";

        String billTo=StringUtils.EMPTY;
        String orderBillTo=StringUtils.EMPTY;//chave do mapa com o centro de custo
        int orderNumber = orders.get(0).getOrderNumber();//apenas � passada uma order de cada vez no construtor
        int num=0;
        List<OrderDetail> orderDetailList;
        String group = new DecimalFormat("00").format(year) + new DecimalFormat("0000").format(orderNumber);


        Map<String, List<OrderDetail>> mapListProductsByOrderAndBillTo = new HashMap<>();//

        for(Order oOrder : orders){//percorre a lista de orders recebidas
            orderDetailList = orderDetailRepository.findByIdOrderAndIdOrderStatus(oOrder.getId(),ScConstants.ID_ORDER_STATUS_DELIVERED);//obtem o vetor de produtos da encomenda

            for(OrderDetail orderDetail : orderDetailList){
                billTo= productRepository.getBillToByIdProduct(orderDetail.getIdProduct());//obtem o centro de custo por id do produto

                if (billTo == null || billTo.trim() == StringUtils.EMPTY) {// no caso de n�o// centro de      // custo   // definido na // BD                             // existir
                    if (idApplication == ApiConstants.TOYOTA_APP) //idApplication ShopCart Toyota
                        billTo = "0238";// centro de custos da toyota
                    else if(idApplication == ApiConstants.LEXUS_APP)
                        billTo = "0288";// no caso lexus
                }
                orderBillTo = oOrder.getOrderNumber()+billTo;// chave constituida por orderNumber + billTo (centro de custo)
                /*
                if(!(mapListProductsByOrderAndBillTo.containsKey(orderBillTo)))
                    mapListProductsByOrderAndBillTo.put(orderBillTo, new Vector<OrderDetail>());
                mapListProductsByOrderAndBillTo.get(orderBillTo).add(orderDetail);
                */
                mapListProductsByOrderAndBillTo.computeIfAbsent(orderBillTo, key -> new ArrayList<>()).add(orderDetail);
            }
        }

        Iterator<Map.Entry<String,  Vector<OrderDetail>>> iterator = mapListProductsByOrderAndBillTo.entrySet().iterator();
        while (iterator.hasNext()) {

            Entry<String, Vector<OrderDetail>> entry = iterator.next();

            Vector<OrderDetail> vecOrderD = mapListProductsByOrderAndBillTo.get(entry.getKey());

            if(entry.getKey().length()>4){
                costCenter = (entry.getKey()).substring(entry.getKey().length()-4);
            }
            Vector<AlObservations> vecObservations = new Vector<AlObservations>();
            Vector<AlMovement>    vecMovement = new Vector<AlMovement>();
            AlObservations alObs = new AlObservations();


            num=0;


            for(OrderDetail OorderD : vecOrderD){

                String registryKeyAl = registryKeyPrefix + new DecimalFormat("0000").format(year)
                        + new DecimalFormat("0000000").format(OorderD.getId_order())+ OorderD.getId();

                AlMovement oAlMovement = new AlMovement();

                oAlMovement.setCostCenter(costCenter);
                oAlMovement.setClientDivisionCode(null);

                oAlMovement.setClientCostCenter(null);
                oAlMovement.setAddressCode("01");

                oAlMovement.setClientNumber(dealer.getToyotaDealerCode());
                oAlMovement.setInvoiceOperation(invoiceOperation);

                oAlMovement.setMovementType(movementType);
                oAlMovement.setCurrencyCode(null);


                price = OorderD.getUnitPrice()/2;
                quantity = OorderD.getOrder_quantity();
                // nota 1: preencher Quantit e Price ou FinalValue
                oAlMovement.setQuantity(quantity);
                oAlMovement.setPrice(price);

                // oAlMovement.setFinalValue(getTotal(orders));

                oAlMovement.setVatRate(FinancialTasks.getVAT());
                oAlMovement.setInvoiceItem("N");
                oAlMovement.setVatSchemeCode("");
                oAlMovement.setVatFree("N");
                oAlMovement.setProgram(program);

                oAlMovement.setRegistryKey(registryKeyAl);

                oAlMovement.setGroup(group);

                oAlMovement.setSapOrder(null);
                oAlMovement.setInvoiceOrderItem(0);

                vecMovement.add(oAlMovement);
                //vecObservations = generateInvoiceDetails(program, registryKey, group, vecOrderD);

                alObs = populateAlObservations("L", OorderD.getProductReference() + " - " + OorderD.getProductName(), program, registryKeyAl, group);
                vecObservations.add(alObs);
                num ++;

                if(num == vecOrderD.size()){
                    alObs = populateAlObservations("L", vecObservations.size() + 1, "A fatura corresponde � vossa comparticipa��o (50%) no custo dos materiais (MPVs) fornecidos. ", program, registryKeyAl, group);
                    vecObservations.add(alObs);

                }

            }
            fileName =  writeFiles(vecMovement, vecObservations, entry.getKey());
            fileNameFinal = fileName +";"+fileName;//como podem ser gerados masi que um ficheiro AL para a mesma order, tenho de atualizar na BD todos os ficheiros gerados e enviados ao AS400
        }

        return fileNameFinal;
    }




}
