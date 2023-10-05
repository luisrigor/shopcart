package com.gsc.shopcart.controller;

import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.dto.entity.EditOrderAjaxDTO;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.model.scart.entity.OrderCart;
import com.gsc.shopcart.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

public interface CatalogController {

    @PostMapping(ApiEndpoints.GET_CART)
    ResponseEntity<?> getCart(@RequestParam Integer idCategory, @RequestParam Integer idCatalog,
                              @RequestBody List<Category> listCategorySelected, @AuthenticationPrincipal UserPrincipal userPrincipal);

    @GetMapping(ApiEndpoints.ORDER_PRODUCTS)
    @ResponseBody
    ResponseEntity<?> getDetailOrderProducts(@AuthenticationPrincipal UserPrincipal user,
                                             @RequestParam(required = false)  List<String> oidDealers);

    @PutMapping(ApiEndpoints.EDIT_ORDER_CART_AJAX)
    @ResponseBody
    ResponseEntity<EditOrderAjaxDTO> editOrderCartAjaxServlet(@AuthenticationPrincipal UserPrincipal user, @RequestParam Integer idOrderCart,
                                                              @RequestParam Integer quantity, @RequestParam Integer multiplier);

    @PostMapping(ApiEndpoints.MOVE_PRODUCT_CART)
    @ResponseBody
    ResponseEntity<List<OrderCart>> moveProductCart(@AuthenticationPrincipal UserPrincipal user,
                                                    @RequestParam Integer idProduct,
                                                    @RequestParam(required = false) Integer idProductVariant,
                                                    @RequestParam(required = false)  String typeSelectProduct);


}
