package com.gsc.shopcart.controller.impl;

import com.gsc.shopcart.controller.OrderController;
import com.gsc.shopcart.dto.entity.InfoProductDTO;
import com.gsc.shopcart.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("${app.basePath}")
@RestController
@CrossOrigin("*")
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    public ResponseEntity<InfoProductDTO> showInfoProduct(Integer idProduct, Integer idCatalog,
                                                          String namespace, String virtualpath) {
        InfoProductDTO infoProduct = orderService.showInfoProduct(idProduct, idCatalog, namespace, virtualpath);
        return ResponseEntity.status(HttpStatus.OK).body(infoProduct);
    }
}
