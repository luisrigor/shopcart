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
@Table(name = "PRODUCT_PROPERTY")
public class ProductProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ID_PRODUCT")
    private Integer idProduct;

    @Column(name = "LABEL")
    private String label;

    @Column(name = "OPTION_VALUE")
    private String optionValue;

    @Column(name = "MAX_LENGHT")
    private Integer maxLenght;

    @Column(name = "DATA_TYPE")
    private String dataType;

    @Column(name = "HELP")
    private String help;

    @Column(name = "STATUS")
    private Character status;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;

    @Column(name = "CHANGED_BY")
    private String changedBy;

    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;

    @Column(name = "MANDATORY")
    private Character mandatory;

    @Column(name = "RANK")
    private Integer rank;
}
