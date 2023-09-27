package com.gsc.shopcart.sample.data.provider;

import com.gsc.shopcart.dto.CartDTO;
import com.gsc.shopcart.dto.InfoProductDTO;
import com.gsc.shopcart.dto.OrderCartProduct;
import com.gsc.shopcart.dto.PromotionsDTO;
import com.gsc.shopcart.model.scart.entity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public static ProductProperty getProductPropertyBuilder() {
        return ProductProperty.builder()
                .id(1)
                .idProduct(2)
                .label("LabelValue")
                .optionValue("OptionValue")
                .maxLenght(10)
                .dataType("DataTypeValue")
                .help("HelpValue")
                .status('A') // Puedes cambiar el valor según corresponda
                .createdBy("CreatedByValue")
                .dtCreated(LocalDateTime.now())
                .changedBy("ChangedByValue")
                .dtChanged(LocalDateTime.now())
                .mandatory('Y') // Puedes cambiar el valor según corresponda
                .rank(3)
                .build();
    }




}
