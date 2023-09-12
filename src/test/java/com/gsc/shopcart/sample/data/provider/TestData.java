package com.gsc.shopcart.sample.data.provider;

import com.gsc.shopcart.dto.PromotionsDTO;
import com.gsc.shopcart.model.scart.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class TestData {

    public static PromotionsDTO getPromotionsData() {
        List<Product> products = new ArrayList<>();

        Product product1 = Product.builder()
                .id(1)
                .ref("R")
                .name("n")
                .description("A")
                .unitPrice(1.0)
                .unitPriceConsult(1)
                .priceRules(1)
                .ivaType("NORMAL")
                .startDate(null)
                .build();

        products.add(product1);

        PromotionsDTO promotionsDTO = PromotionsDTO.builder()
                .vecProducts(products)
                .view("V")
                .idCategory("1")
                .viewOnlyPromotions("S")
                .build();

        return promotionsDTO;
    }



}
