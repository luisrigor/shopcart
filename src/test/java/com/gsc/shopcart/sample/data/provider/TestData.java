package com.gsc.shopcart.sample.data.provider;

import com.gsc.shopcart.dto.CartDTO;
import com.gsc.shopcart.dto.OrderCartProduct;
import com.gsc.shopcart.dto.PromotionsDTO;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.model.scart.entity.OrderCart;
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




}
