package com.gsc.shopcart.controller;

import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.dto.entity.InfoProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


public interface OrderController {

    @GetMapping(ApiEndpoints.SHOW_INFO_PRODUCT)
    ResponseEntity<InfoProductDTO> showInfoProduct(@RequestParam Integer idProduct, @RequestParam Integer idCatalog,
                                                   @RequestParam String namespace, @RequestParam String virtualpath);
}
