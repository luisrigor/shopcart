package com.gsc.shopcart.sample.data.provider;

import com.gsc.shopcart.dto.*;
import com.gsc.shopcart.model.scart.entity.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

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

    public static CartDTO getCartData() {

        List<Category> listCategorySelected = new ArrayList<>();

        Category categoryL1 = Category.builder()
                .id(1)
                .idParent(1)
                .name("n")
                .description("")
                .path("p")
                .status("s")
                .build();

        listCategorySelected.add(categoryL1);

        List<Category> categories = new ArrayList<>();

        Category category1 = Category.builder()
                .id(8)
                .idParent(4)
                .name("B")
                .description("")
                .path("P")
                .status("ACTIVO")
                .build();
        categories.add(category1);

        List<OrderCart> orderCarts = new ArrayList<>();

        OrderCart orderCart1 = OrderCart.builder()
                .id(1)
                .idUser(2)
                .idCatalog(3)
                .quantity(4)
                .observations("T")
                .build();

        orderCarts.add(orderCart1);

        return CartDTO.builder()
                .idCategory(1)
                .listCategorySelected(listCategorySelected)
                .vecCategories(categories)
                .vecOrderCart(orderCarts)
                .virtualPath("T")
                .idCatalog("1")
                .build();
    }

    public static PromotionsDTO getProductsByFreeSearchData() {
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
                .view("BACKOFFICE")
                .idCategory("-1")
                .build();

        return promotionsDTO;
    }

    public static InfoProductDTO getInfoProductData() {


        Product product = Product.builder()
                .id(1)
                .ref("r")
                .name("n")
                .description("d")
                .unitPrice(1.0)
                .unitPriceConsult(0)
                .priceRules(1)
                .ivaType("E")
                .build();

        List<ProductItem> vecProductItem = new ArrayList<>();
        List<ProductPriceRule> vecProductPriceRules = new Vector();

        ProductPriceRule productPriceRule1 = ProductPriceRule.builder()
                .id(1)
                .idProduct(2)
                .minimumQuantity(10)
                .incrementalQuantity(5)
                .unitPrice(1.0)
                .createdBy("test")
                .build();

        ProductPriceRule productPriceRule2 = ProductPriceRule.builder()
                .id(2)
                .idProduct(21)
                .minimumQuantity(10)
                .incrementalQuantity(5)
                .unitPrice(1.0)
                .createdBy("test")
                .build();

        vecProductPriceRules.add(productPriceRule1);
        vecProductPriceRules.add(productPriceRule2);


        return InfoProductDTO.builder()
                .product(product)
                .namespace("namespace")
                .virtualpath("virtualpath")
                .productItemList(vecProductItem)
                .productPriceRules(vecProductPriceRules)
                .idCatalog(String.valueOf("idCatalog"))
                .build();
    }

    public static CreateProductDTO createSampleProductDTO() {
        CreateProductDTO productDTO = new CreateProductDTO();
        productDTO.setIdProduct(0);
        productDTO.setIdCategory(1);

        List<String> dealers = new ArrayList<>();

        dealers.add("SC01");
        dealers.add("SC02");

        List<String> minimun_qtd = Arrays.asList("300","300","300");
        List<String> incremental_qtd =  Arrays.asList("10","10","10");
        List<String> unit_price_qtd = Arrays.asList("5","5","5");

        CreateProdPriceRule prodPriceRule =  CreateProdPriceRule.builder()
                .minimun_qtd(minimun_qtd)
                .incremental_qtd(incremental_qtd)
                .unit_price_qtd(unit_price_qtd)
                .build();

        productDTO.setDealers(dealers);
        productDTO.setProdPriceRule(prodPriceRule);

        return productDTO;
    }



}
