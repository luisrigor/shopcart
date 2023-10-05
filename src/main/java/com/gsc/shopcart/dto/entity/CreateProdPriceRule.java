package com.gsc.shopcart.dto.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProdPriceRule {

    List<String> minimun_qtd;
    List<String> incremental_qtd;
    List<String> unit_price_qtd;
}
