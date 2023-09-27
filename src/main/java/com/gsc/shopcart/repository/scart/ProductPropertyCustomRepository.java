package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.dto.ProductPropertyOrder;

import java.util.List;

public interface ProductPropertyCustomRepository {

    List<ProductPropertyOrder> getProductPropertyByIdProduct(Integer idProduct, String status);
}
