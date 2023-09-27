package com.gsc.shopcart.dto;

import com.gsc.shopcart.model.scart.entity.OrderCart;
import com.rg.dealer.Dealer;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProductsDTO {

    private Map<String,String> allServices;
    private List<OrderCart> vecOrderCart;
    private  Map<String, Dealer> hstDealers;
    private Map<Integer,String> suppliers;
    private List<Dealer> dealers;
    private Map<String, List<Dealer>> addresses;

}
