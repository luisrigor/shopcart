package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.Product;

import java.util.List;

public interface ProductCustomRepository {
    List<Product> getProductsInPromotion(int idRootCategory);
    List<Product> getProductsByIdCategory(int idCategory, String view, String userOidDealer);

}
