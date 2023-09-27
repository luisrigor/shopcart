package com.gsc.shopcart.controller;

import com.google.gson.Gson;
import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.dto.CartDTO;
import com.gsc.shopcart.dto.OrderProductsDTO;
import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.model.usrlogon.entity.CbusDealer;
import com.gsc.shopcart.model.usrlogon.entity.LexusDealer;
import com.gsc.shopcart.model.usrlogon.entity.ToyotaDealer;
import com.gsc.shopcart.repository.usrlogon.CbusDealerRepository;
import com.gsc.shopcart.repository.usrlogon.LexusDealerRepository;
import com.gsc.shopcart.repository.usrlogon.ToyotaDealerRepository;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.service.CatalogService;

import com.rg.dealer.Dealer;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("${app.baseUrl}")
@Api(value = "",tags = "CATALOG")
@RestController
@CrossOrigin("*")
public class CatalogController {

    private final CatalogService catalogService;


    @PostMapping(ApiEndpoints.GET_CART)
    public ResponseEntity<?> getCart(@RequestParam Integer idCategory, @RequestParam Integer idCatalog,
                                     @RequestBody List<Category> listCategorySelected, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        CartDTO cartDTO = catalogService.getCart(idCategory, idCatalog, listCategorySelected, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(cartDTO);
    }

    @GetMapping(ApiEndpoints.ORDER_PRODUCTS)
    @ResponseBody
    public ResponseEntity<?> getDetailOrderProducts(@AuthenticationPrincipal UserPrincipal user,
                                                                   @RequestParam(required = false)  List<String> oidDealers) {
        Gson gson = new Gson();
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(catalogService.getDetailOrderProducts(user,oidDealers)));
    }
}
