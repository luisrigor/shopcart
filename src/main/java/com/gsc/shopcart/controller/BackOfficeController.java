package com.gsc.shopcart.controller;

import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.dto.PromotionsDTO;
import com.gsc.shopcart.service.BackOfficeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("${app.baseUrl}")
@Api(value = "",tags = "CART")
@RestController
@CrossOrigin("*")
public class BackOfficeController {

    private final BackOfficeService backOfficeService;

    @GetMapping(ApiEndpoints.GET_PROMOTIONS)
    public ResponseEntity<PromotionsDTO> getPromotions(@RequestParam Integer idCatalog) {
        PromotionsDTO promotions = backOfficeService.getPromotions(idCatalog);

        return ResponseEntity.status(HttpStatus.OK).body(promotions);
    }
}
