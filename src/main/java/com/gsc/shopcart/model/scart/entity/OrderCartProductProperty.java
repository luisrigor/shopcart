package com.gsc.shopcart.model.scart.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ORDER_CART_PRODUCT_PROPERTY")
public class OrderCartProductProperty {

    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ID_ORDER_CART")
    private Integer idOrderCart;
    @Column(name = "ID_PRODUCT_PROPERTY")
    private Integer idProductProperty;
    @Column(name = "INPUT_VALUE")
    private String inputValue;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;
    @Column(name = "CHANGED_BY")
    private String changedBy;
    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;

}
