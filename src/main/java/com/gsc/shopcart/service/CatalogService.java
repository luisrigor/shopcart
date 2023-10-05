package com.gsc.shopcart.service;

import com.gsc.shopcart.dto.entity.CartDTO;
import com.gsc.shopcart.dto.entity.EditOrderAjaxDTO;
import com.gsc.shopcart.dto.entity.OrderProductsDTO;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.model.scart.entity.OrderCart;
import com.gsc.shopcart.security.UserPrincipal;

import java.util.List;

public interface CatalogService {

    CartDTO getCart(Integer idCategory, Integer idCatalog, List<Category> listCategorySelected, UserPrincipal userPrincipal);

    OrderProductsDTO getDetailOrderProducts(UserPrincipal user, List<String> oidDealers);

    EditOrderAjaxDTO editOrderCartAjaxServlet(Integer idOrderCart, Integer quantity, Integer multiplier, UserPrincipal user);

    List<OrderCart> moveProductToCart(Integer idProductParam, Integer idProductVariantParam, String typeSelectProductParam, UserPrincipal user);
}
