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
@Table(name = "PRODUCT_ATTRIBUTES", schema = "DB2INST1")
public class ProductAttribute {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ID_PRODUCT")
    private Integer idProduct;

    @Column(name = "FIELD1")
    private String field1;

    @Column(name = "FIELD2")
    private String field2;

    @Column(name = "FIELD3")
    private String field3;

    @Column(name = "FIELD4")
    private String field4;

    @Column(name = "FIELD5")
    private String field5;

    @Column(name = "FIELD6")
    private String field6;

    @Column(name = "FIELD7")
    private String field7;

    @Column(name = "FIELD8")
    private String field8;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;

    @Column(name = "CHANGED_BY")
    private String changedBy;

    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;
}
