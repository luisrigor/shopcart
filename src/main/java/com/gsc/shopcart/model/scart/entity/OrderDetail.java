package com.gsc.shopcart.model.scart.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "ORDER_DETAIL")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ID_ORDER")
    private Integer idOrder;

    @Column(name = "ID_PRODUCT")
    private Integer idProduct;

    @Column(name = "ID_ORDER_STATUS")
    private Integer idOrderStatus;

    @Column(name = "ID_SUPPLIER")
    private Integer idSupplier;

    @Column(name = "UNITPRICE")
    private Double unitPrice;

    @Column(name = "UNIT_PRICE_RULE")
    private Double unitPriceRule;

    @Column(name = "PRICE")
    private Double price;

    @Column(name = "PRICE_OBS")
    private String priceObs;

    @Column(name = "IVA_TYPE")
    private String ivaType;

    @Column(name = "VALUE_IVA")
    private Double valueIva;

    @Column(name = "ORDER_QUANTITY")
    private Integer orderQuantity;

    @Column(name = "RECEIVED_QUANTITY")
    private Integer receivedQuantity;

    @Column(name = "DT_DELIVERED")
    private LocalDateTime dtDelivered;

    @Column(name = "DELIVERED_BY")
    private String deliveredBy;

    @Column(name = "DELIVERED_OBS")
    private String deliveredObs;

    @Column(name = "DT_RECEIVED")
    private LocalDateTime dtReceived;

    @Column(name = "RECEIVED_BY")
    private String receivedBy;

    @Column(name = "RECEIVED_OBS")
    private String receivedObs;

    @Column(name = "DT_CANCEL")
    private LocalDateTime dtCancel;

    @Column(name = "CANCEL_BY")
    private String cancelBy;

    @Column(name = "CANCEL_OBS")
    private String cancelObs;

    @Column(name = "DT_EXPECTED_DELIVERY")
    private LocalDateTime dtExpectedDelivery;

    @Column(name = "EXPECTED_DELIVERY_BY")
    private String expectedDeliveryBy;

    @Column(name = "EXPECTED_DELIVERY_OBS")
    private String expectedDeliveryObs;

    @Column(name = "PRODUCT_REFERENCE")
    private String productReference;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "SKU")
    private String sku;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "SIZE")
    private String size;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;

    @Column(name = "CHANGED_BY")
    private String changedBy;

    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;
/*
    protected Product ivProduct;
    protected ProductVariant ivProductVariant;
    protected OrderStatus ivOrderStatus;
    protected Order ivOrder;
    protected Vector<ProductProperty> ivVecOrderDetailProductProperty;

 */


}
