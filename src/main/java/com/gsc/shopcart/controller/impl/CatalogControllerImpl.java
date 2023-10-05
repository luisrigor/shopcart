package com.gsc.shopcart.controller.impl;

import com.google.gson.Gson;
import com.gsc.shopcart.controller.CatalogController;
import com.gsc.shopcart.dto.CartDTO;
import com.gsc.shopcart.dto.EditOrderAjaxDTO;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.model.scart.entity.OrderCart;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CatalogControllerImpl implements CatalogController {

    private final CatalogService catalogService;


    @Override
    public ResponseEntity<?> getCart(Integer idCategory,Integer idCatalog,
                                     List<Category> listCategorySelected,UserPrincipal userPrincipal) {
        CartDTO cartDTO = catalogService.getCart(idCategory, idCatalog, listCategorySelected, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(cartDTO);
    }

    @Override
    public ResponseEntity<?> getDetailOrderProducts(UserPrincipal user, List<String> oidDealers) {
        Gson gson = new Gson();
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(catalogService.getDetailOrderProducts(user,oidDealers)));
    }

    @Override
    public ResponseEntity<EditOrderAjaxDTO> editOrderCartAjaxServlet(UserPrincipal user,Integer idOrderCart,
                                                                     Integer quantity,Integer multiplier) {
        return ResponseEntity.status(HttpStatus.OK).body(catalogService.editOrderCartAjaxServlet(idOrderCart,quantity,multiplier,user));
    }

    @Override
    public ResponseEntity<List<OrderCart>> moveProductCart(UserPrincipal user, Integer idProduct,
                                                           Integer idProductVariant, String typeSelectProduct) {
        return ResponseEntity.status(HttpStatus.OK).body(catalogService.moveProductToCart(idProduct,idProductVariant,typeSelectProduct,user));
    }
}
