package com.gsc.shopcart.controller;

import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.dto.InfoProductDTO;
import com.gsc.shopcart.service.OrderService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("${app.baseUrl}")
@Api(value = "",tags = "ORDER")
@RestController
@CrossOrigin("*")
public class OrderController {

    private final OrderService orderService;

    @GetMapping(ApiEndpoints.SHOW_INFO_PRODUCT)
    public ResponseEntity<InfoProductDTO> showInfoProduct(@RequestParam Integer idProduct, @RequestParam Integer idCatalog,
                                                          @RequestParam String namespace, @RequestParam String virtualpath) {
        InfoProductDTO infoProduct = orderService.showInfoProduct(idProduct, idCatalog, namespace, virtualpath);
        return ResponseEntity.status(HttpStatus.OK).body(infoProduct);
    }
}
