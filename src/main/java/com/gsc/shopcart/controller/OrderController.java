package com.gsc.shopcart.controller;

import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.dto.InfoProductDTO;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("${app.baseUrl}")
@Api(value = "",tags = "ORDER")
@RestController
@CrossOrigin("*")
public interface OrderController {

    @GetMapping(ApiEndpoints.SHOW_INFO_PRODUCT)
    ResponseEntity<InfoProductDTO> showInfoProduct(@RequestParam Integer idProduct, @RequestParam Integer idCatalog,
                                                   @RequestParam String namespace, @RequestParam String virtualpath);
}
