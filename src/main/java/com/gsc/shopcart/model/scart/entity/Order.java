package com.gsc.shopcart.model.scart.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "ORDER")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ID_CATALOG")
    private Integer idCatalog;
    @Column(name = "OID_DEALER")
    private String oidDealer;
    @Column(name = "DT_ORDER")
    private LocalDateTime dtOrder;
    @Column(name = "ORDER_OBS")
    private String orderObs;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "ID_USER")
    private Integer idUser;
    @Column(name = "DELIVERY_OID_DEALER")
    private String deliveryOidDealer;
    @Column(name = "DELIVERY_NAME")
    private String deliveryName;
    @Column(name = "DELIVERY_EMAIL")
    private String deliveryEmail;
    @Column(name = "DELIVERY_PHONE")
    private String deliveryPhone;
    @Column(name = "DELIVERY_ADDRESS")
    private String deliveryAddress;
    @Column(name = "DELIVERY_CP4")
    private String deliveryCp4;
    @Column(name = "DELIVERY_CP3")
    private String deliveryCp3;
    @Column(name = "DELIVERY_CPEXT")
    private String deliveryCpExt;
    @Column(name = "INVOICE_NIF")
    private String invoiceNif;
    @Column(name = "INVOICE_NAME")
    private String invoiceName;
    @Column(name = "INVOICE_ADDRESS")
    private String invoiceAddress;
    @Column(name = "INVOICE_CP4")
    private String invoiceCp4;
    @Column(name = "INVOICE_CP3")
    private String invoiceCp3;
    @Column(name = "INVOICE_CPEXT")
    private String invoiceCpExt;
    @Column(name = "IPEC")
    private String ipec;
    @Column(name = "SHIPPING_COST")
    private Double shippingCost;
    @Column(name = "TOTAL")
    private Double total;
    @Column(name = "ORDER_NUMBER")
    private Integer orderNumber;
    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;
    @Column(name = "CHANGED_BY")
    private String changedBy;
    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;
    @Column(name = "DT_AL_GENERATED")
    private LocalDateTime dtAlGenerated;
    @Column(name = "AL_GENERATED_FILE_NAME")
    private String alGeneratedFileName;
    @Transient
    private List<OrderDetail> vecOrderDetail;
}
