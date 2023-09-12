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
@Table(name = "CATALOG")
public class Catalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ID_ROOT_CATEGORY")
    private Integer idRootCategory;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;

    @Column(name = "UPLOAD_DIR")
    private String uploadDir;

    @Column(name = "VIRTUAL_PATH")
    private String virtualPath;

    @Column(name = "APPLICATION")
    private String application;

    @Column(name = "TCAP_PROFILE")
    private Integer tcapProfile;

    @Column(name = "DEALER_PROFILE")
    private Integer dealerProfile;

    @Column(name = "SUPPLIER_PROFILE")
    private Integer supplierProfile;

    @Column(name = "OID_NET")
    private String oidNet;

    @Column(name = "HAS_PRICE_CONSULT")
    private Character hasPriceConsult;

    @Column(name = "HAS_PRICE_RULES")
    private Character hasPriceRules;

    @Column(name = "HAS_PRODUCT_PROPERTY")
    private Character hasProductProperty;

    @Column(name = "SHOW_PUBLIC_PRICE")
    private Character showPublicPrice;

    @Column(name = "HAS_PRODUCT_VARIENT")
    private Character hasProductVarient;

    @Column(name = "HAS_RELATED_PRODUCTS")
    private Character hasRelatedProducts;

    @Column(name = "IS_TO_SEND_TO_AS400")
    private Character isToSendToAs400;

    @Column(name = "ALLOW_ACCESS_WITHOUT_PROFILE")
    private Character allowAccessWithoutProfile;

    @Column(name = "SHOW_SHOPCART")
    private Character showShopCart;

    @Column(name = "SHOW_FILTER_ORDERS")
    private Character showFilterOrders;
}
