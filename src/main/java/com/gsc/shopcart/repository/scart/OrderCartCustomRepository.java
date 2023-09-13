package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.dto.OrderCartProduct;

import java.util.List;

public interface OrderCartCustomRepository {

    List<OrderCartProduct> getOrderCartByIdUserAndIdCatalog(Integer idUser, Integer idCatalog);
}
