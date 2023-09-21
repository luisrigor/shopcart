package com.gsc.shopcart.controller;

import com.google.gson.Gson;
import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.dto.GetOrderStateDTO;
import com.gsc.shopcart.dto.ListOrderDTO;
import com.gsc.shopcart.dto.OrderStateDTO;
import com.gsc.shopcart.dto.SendInvoiceDTO;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.service.OrderStateService;
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

    private final OrderStateService orderStateService;

    @PostMapping(ApiEndpoints.GET_ORDER_STATE)
    public ResponseEntity<?> getOrderState(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody GetOrderStateDTO getOrderStateDTO) {
        Gson gson = new Gson();
        OrderStateDTO orderStateDTO = orderStateService.getOrderState(userPrincipal,getOrderStateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(orderStateDTO));
    }

    @PostMapping(ApiEndpoints.SEND_INVOICE)
    public ResponseEntity<String> sendInvoice(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody SendInvoiceDTO sendInvoiceDTO){
        orderStateService.sendInvoice(userPrincipal,sendInvoiceDTO.getOrderList());
        return ResponseEntity.status(HttpStatus.OK).body("Send Invoice Successfully Executed");
    }

    @GetMapping(ApiEndpoints.LIST_ORDER_DETAIL)
    public ResponseEntity<ListOrderDTO> listOrderDetail(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Integer idOrder,
                                                        @RequestParam Integer idOrderDetailStatus){
        return ResponseEntity.status(HttpStatus.OK).body(orderStateService.listOrderDetail(userPrincipal,idOrder,idOrderDetailStatus));
    }

}
