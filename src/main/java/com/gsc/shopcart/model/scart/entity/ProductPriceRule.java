package com.gsc.shopcart.model.scart.entity;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PRODUCT_PRICE_RULES")
public class ProductPriceRule {


    @Id
    @GeneratedValue(generator = "max_plus_id")
    @GenericGenerator(
            name = "max_plus_id",
            strategy = "com.gsc.shopcart.model.scart.entity.LastIdGenerator",
            parameters = @org.hibernate.annotations.Parameter(name = "entityClassName", value = "PRODUCT_PRICE_RULES")
    )
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ID_PRODUCT")
    private Integer idProduct;

    @Column(name = "MINIMUM_QUANTITY")
    private Integer minimumQuantity;

    @Column(name = "INCREMENTAL_QUANTITY")
    private Integer incrementalQuantity;

    @Column(name = "UNIT_PRICE")
    private Double unitPrice;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;

    @Column(name = "CHANGED_BY")
    private String changedBy;

    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;

}
