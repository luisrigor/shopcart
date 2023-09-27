package com.gsc.shopcart.service;

import com.gsc.shopcart.dto.CartDTO;
import com.gsc.shopcart.dto.EditOrderAjaxDTO;
import com.gsc.shopcart.dto.OrderProductsDTO;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.security.UserPrincipal;

import java.util.List;
import java.util.Map;

public interface CatalogService {

    CartDTO getCart(Integer idCategory, Integer idCatalog, List<Category> listCategorySelected, UserPrincipal userPrincipal);

    OrderProductsDTO getDetailOrderProducts(UserPrincipal user, List<String> oidDealers);

    EditOrderAjaxDTO editOrderCartAjaxServlet(Integer idOrderCart, Integer quantity, Integer multiplier, UserPrincipal user);
}
