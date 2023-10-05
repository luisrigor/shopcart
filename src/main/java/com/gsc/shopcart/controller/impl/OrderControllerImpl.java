package com.gsc.shopcart.controller.impl;

import com.gsc.shopcart.controller.OrderController;
import com.gsc.shopcart.dto.InfoProductDTO;
import com.gsc.shopcart.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    public ResponseEntity<InfoProductDTO> showInfoProduct(Integer idProduct,Integer idCatalog,
                                                          String namespace,String virtualpath) {
        InfoProductDTO infoProduct = orderService.showInfoProduct(idProduct, idCatalog, namespace, virtualpath);
        return ResponseEntity.status(HttpStatus.OK).body(infoProduct);
    }
}
