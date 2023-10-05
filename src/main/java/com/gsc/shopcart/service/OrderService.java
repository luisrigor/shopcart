package com.gsc.shopcart.service;


import com.gsc.shopcart.dto.entity.InfoProductDTO;

public interface OrderService {

    InfoProductDTO showInfoProduct(Integer idProduct, Integer idCatalog, String namespace, String virtualpath);
}
