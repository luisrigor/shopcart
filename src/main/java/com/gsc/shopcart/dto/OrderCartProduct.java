package com.gsc.shopcart.dto;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderCartProduct {

    private Integer id;
    private Integer idUser;
    private Integer idCatalog;
    private Integer idProduct;
    private Integer quantity;
    private String observations;
    private Integer unitPriceRule;
    private String createdBy;
    private LocalDateTime dtCreated;
    private String changedBy;
    private LocalDateTime dtChanged;
    private Double price;
    private Integer idProductVariant;

    private String name;
    private String thumbnailPath;
    private Double unitPrice;
    private String ivaType;
    private double total;
    private Integer priceRules;
    private LocalDate promoStart;
    private LocalDate promoEnd;
    private Double promoPrice;
    private Long numOfProductProperties;


}
