package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.dto.RelatedProduct;
import com.gsc.shopcart.dto.ShopCartFilter;
import com.gsc.shopcart.model.scart.entity.Product;

import java.util.List;

public interface ProductCustomRepository {
    List<Product> getProductsInPromotion(int idRootCategory);
    List<Product> getProductsByIdCategory(int idCategory, String view, String userOidDealer);
    List<Product> getProductsByFreeSearch(int idRootCategory, String view, String userOidDealer, ShopCartFilter filter);
    List<RelatedProduct> getRelatedProducts(Integer idRootCategory, Integer idProduct);
    void mergeRelatedProducts(int idProduct1, int idProduct2, String changedBy);

}
