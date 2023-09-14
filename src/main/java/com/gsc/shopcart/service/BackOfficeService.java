package com.gsc.shopcart.service;

import com.gsc.shopcart.dto.PromotionsDTO;
import com.gsc.shopcart.dto.ShopCartFilter;

public interface BackOfficeService {
    PromotionsDTO getPromotions(Integer idCatalog);
    PromotionsDTO getProductsByFreeSearch(Integer idCategory, Integer idCatalog, ShopCartFilter filter);
}
