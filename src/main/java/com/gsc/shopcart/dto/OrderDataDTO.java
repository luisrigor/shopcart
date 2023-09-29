package com.gsc.shopcart.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDataDTO {

    private List<Integer> idsProduct;
    private List<Integer> idsProductVariant;
    private List<Integer> idsOrderCart;
    private List<Integer> unitPriceProduct;
    private List<Integer> unitPriceProductRule;
    private List<Integer> ivaTypeProduct;
    private List<Integer> valueIvaProduct;
    private List<Integer>orderQuantity;
    private List<Integer> idSupplier;
    private List<Integer> hasPriceRules;
    private List<Integer>observationsOrderCart;
    private List<Integer> priceOrderCart;
    private List<String> oidDealer;
    private String orderObs;
    private Integer multiplicator;
    private String client;
    private String shipTO;
    private String sendToAS400;

}
