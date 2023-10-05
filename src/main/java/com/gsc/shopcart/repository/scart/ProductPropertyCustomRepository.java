package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.dto.ProductPropertyInputValue;
import com.gsc.shopcart.dto.ProductPropertyOrder;
import com.gsc.shopcart.model.scart.entity.ProductProperty;

import java.util.List;

public interface ProductPropertyCustomRepository {

    List<ProductPropertyOrder> getProductPropertyByIdProduct(Integer idProduct, String status);

    List<ProductPropertyInputValue> getOrderCartProductProperty(Integer idOrderCart, Integer idProduct, String mandatory);
}
