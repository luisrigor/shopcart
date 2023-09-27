package com.gsc.shopcart.sample.data.provider;

import com.gsc.shopcart.dto.GetOrderStateDTO;
import com.gsc.shopcart.dto.OrderProductsDTO;
import com.gsc.shopcart.dto.OrderStateDTO;
import com.gsc.shopcart.model.scart.entity.Order;
import com.gsc.shopcart.model.scart.entity.OrderDetail;
import com.gsc.shopcart.model.scart.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class OrderData {

    public static OrderStateDTO getOrderStatusDTO() {
        return OrderStateDTO.builder()
                .vecDealers(new ArrayList<>())
                .hstDealers(new HashMap<>())
                .vecOrderState(new ArrayList<>())
                .vecOrderStatus(new ArrayList<>())
                .hmOrderDetails(new HashMap<>())
                .suppliers(new HashMap<>())
                .users(new LinkedHashMap<>())
                .idCatalog(1)
                .preferences(new HashMap<>())
                .build();
    }
    public static GetOrderStateDTO getGetOrderStateDTO(){
        return GetOrderStateDTO.builder()
                .idCatalog(1)
                .idOrderStatus(4)
                .idSupplier(5)
                .orderNr(8)
                .iPec("IPecValue")
                .reference("ReferenceValue")
                .oidParent("OidParentValue")
                .orderType("OrderTypeValue")
                .build();
    }

    public static Order getOrderBuilder() {
        return Order.builder()
                .id(1)
                .idCatalog(1)
                .oidDealer("DealerOIDValue")
                .dtOrder(LocalDateTime.now())
                .orderObs("OrderObsValue")
                .createdBy("CreatedByValue")
                .idUser(2)
                .deliveryOidDealer("DeliveryDealerOIDValue")
                .deliveryName("DeliveryNameValue")
                .deliveryEmail("DeliveryEmailValue")
                .deliveryPhone("DeliveryPhoneValue")
                .deliveryAddress("DeliveryAddressValue")
                .deliveryCp4("DeliveryCP4Value")
                .deliveryCp3("DeliveryCP3Value")
                .deliveryCpExt("DeliveryCPExtValue")
                .invoiceNif("InvoiceNifValue")
                .invoiceName("InvoiceNameValue")
                .invoiceAddress("InvoiceAddressValue")
                .invoiceCp4("InvoiceCP4Value")
                .invoiceCp3("InvoiceCP3Value")
                .invoiceCpExt("InvoiceCPExtValue")
                .ipec("IpecValue")
                .shippingCost(10.0)
                .total(100.0)
                .orderNumber(12345)
                .dtCreated(LocalDateTime.now())
                .changedBy("ChangedByValue")
                .dtChanged(LocalDateTime.now())
                .dtAlGenerated(LocalDateTime.now())
                .alGeneratedFileName("AlGeneratedFileNameValue")
                .build();
    }

    public static OrderDetail getOrderDetailBuilder() {
        return OrderDetail.builder()
                .id(1)
                .idOrder(1)
                .idProduct(2)
                .idOrderStatus(3)
                .idSupplier(4)
                .unitPrice(10.0)
                .unitPriceRule(9.0)
                .price(90.0)
                .priceObs("PriceObsValue")
                .ivaType("IVAValue")
                .valueIva(9.0)
                .orderQuantity(5)
                .receivedQuantity(5)
                .dtDelivered(LocalDateTime.now())
                .deliveredBy("DeliveredByValue")
                .deliveredObs("DeliveredObsValue")
                .dtReceived(LocalDateTime.now())
                .receivedBy("ReceivedByValue")
                .receivedObs("ReceivedObsValue")
                .dtCancel(LocalDateTime.now())
                .cancelBy("CancelByValue")
                .cancelObs("CancelObsValue")
                .dtExpectedDelivery(LocalDateTime.now())
                .expectedDeliveryBy("ExpectedDeliveryByValue")
                .expectedDeliveryObs("ExpectedDeliveryObsValue")
                .productReference("ProductReferenceValue")
                .productName("ProductNameValue")
                .sku("SKUValue")
                .name("NameValue")
                .description("DescriptionValue")
                .color("ColorValue")
                .size("SizeValue")
                .createdBy("CreatedByValue")
                .dtCreated(LocalDateTime.now())
                .changedBy("ChangedByValue")
                .dtChanged(LocalDateTime.now())
                .build();
    }

    public static OrderStatus getOrderStatusBuilder() {
        return OrderStatus.builder()
                .id(1)
                .status("StatusValue")
                .description("DescriptionValue")
                .createdBy("CreatedByValue")
                .dtCreated(LocalDateTime.now())
                .changedBy("ChangedByValue")
                .dtChanged(LocalDateTime.now())
                .build();
    }

    public static OrderProductsDTO getOrderProductsDTOBuilder() {
        return OrderProductsDTO.builder()
                .allServices(Collections.emptyMap())
                .vecOrderCart(Collections.emptyList())
                .hstDealers(Collections.emptyMap())
                .suppliers(Collections.emptyMap())
                .dealers(Collections.emptyList())
                .addresses(Collections.emptyMap())
                .build();
    }



}
