package com.gsc.shopcart.service;

import com.gsc.shopcart.dto.CartDTO;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.security.UserPrincipal;

import java.util.List;

public interface CatalogService {

    CartDTO getCart(Integer idCategory, Integer idCatalog, List<Category> listCategorySelected, UserPrincipal userPrincipal);
}
