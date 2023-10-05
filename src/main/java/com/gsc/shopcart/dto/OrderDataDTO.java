package com.gsc.shopcart.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDataDTO {

    @NotNull
    private List<Integer> idsProduct;
    @NotNull
    private List<String> idsProductVariant;
    @NotNull
    private List<Integer> idsOrderCart;
    @NotNull
    private List<Double> unitPriceProduct;
    @NotNull
    private List<Double> unitPriceProductRule;
    @NotNull
    private List<String> ivaTypeProduct;
    @NotNull
    private List<Double> valueIvaProduct;
    @NotNull
    private List<Integer> orderQuantity;
    @NotNull
    private List<Integer> idSupplier;
    @NotNull
    private List<String> hasPriceRules;
    @NotNull
    private List<String> observationsOrderCart;
    @NotNull
    private List<Double> priceOrderCart;

    private String orderObs;
    private Integer multiplicator;
    private String client;
    private String shipTO;
    private String sendToAS400;
    private Integer idCategory;

    private Boolean hasProductProperties;

}
