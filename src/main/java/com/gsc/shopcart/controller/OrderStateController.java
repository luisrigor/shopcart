package com.gsc.shopcart.controller;

import com.google.gson.Gson;
import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.dto.GetOrderStateDTO;
import com.gsc.shopcart.dto.OrderStateDTO;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.service.OrderStatusService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("${app.baseUrl}")
@Api(value = "",tags = "ORDER")
@RestController
@CrossOrigin("*")
public class OrderStateController {

    private final OrderStatusService orderStatusService;

    @PostMapping(ApiEndpoints.GET_ORDER_STATE)
    public ResponseEntity<?> getOrderState(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody GetOrderStateDTO getOrderStateDTO) {
        Gson gson = new Gson();
        OrderStateDTO orderStateDTO = orderStatusService.getOrderState(userPrincipal,getOrderStateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(orderStateDTO));
    }


}
