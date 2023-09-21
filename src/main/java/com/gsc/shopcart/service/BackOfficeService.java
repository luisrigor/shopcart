package com.gsc.shopcart.service;

import com.gsc.shopcart.dto.PromotionsDTO;
import com.gsc.shopcart.dto.ShopCartFilter;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.security.UserPrincipal;

import java.util.List;

public interface BackOfficeService {
    PromotionsDTO getPromotions(Integer idCatalog, Integer idUser, Boolean isCatalog);
    PromotionsDTO getProductsByFreeSearch(Integer idCategory, Integer idCatalog, ShopCartFilter filter, Boolean isCatalog, UserPrincipal userPrincipal);
}
