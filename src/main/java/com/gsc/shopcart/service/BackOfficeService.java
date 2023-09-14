package com.gsc.shopcart.service;

import com.gsc.shopcart.dto.PromotionsDTO;
import com.gsc.shopcart.security.UserPrincipal;

public interface BackOfficeService {

    PromotionsDTO getPromotions(Integer idCatalog, UserPrincipal user);
}
