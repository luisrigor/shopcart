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
@Table(name = "CATEGORY_PRODUCTS")
public class CategoryProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CATEGORY")
    private Integer id;

    @Column(name = "ID_PRODUCT")
    private Integer idProduct;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;
}
