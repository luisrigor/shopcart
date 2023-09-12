package com.gsc.shopcart.service;

import com.gsc.shopcart.dto.PromotionsDTO;

public interface BackOfficeService {

    PromotionsDTO getPromotions(Integer idCatalog);
}
