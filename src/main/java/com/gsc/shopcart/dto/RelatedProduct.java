package com.gsc.shopcart.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelatedProduct {

    private Integer idProduct;
    private String ref;
    private String productName;
    private String isRelatedProduct;
}
