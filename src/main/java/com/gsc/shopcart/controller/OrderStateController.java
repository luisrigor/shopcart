package com.gsc.shopcart.controller;

import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.dto.entity.GetOrderStateDTO;
import com.gsc.shopcart.dto.entity.ListOrderDTO;
import com.gsc.shopcart.dto.entity.SendInvoiceDTO;
import com.gsc.shopcart.model.scart.entity.OrderDetail;
import com.gsc.shopcart.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


public interface OrderStateController {

    @PostMapping(ApiEndpoints.GET_ORDER_STATE)
    ResponseEntity<?> getOrderState(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody GetOrderStateDTO getOrderStateDTO);


    @PostMapping(ApiEndpoints.SEND_INVOICE)
    ResponseEntity<String> sendInvoice(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody SendInvoiceDTO sendInvoiceDTO);


    @GetMapping(ApiEndpoints.LIST_ORDER_DETAIL)
    ResponseEntity<ListOrderDTO> listOrderDetail(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Integer idOrder,
                                                 @RequestParam(required = false) Integer idOrderDetailStatus);


    @GetMapping(ApiEndpoints.CHANGE_ORDER_DETAIL_STATUS)
    ResponseEntity<OrderDetail> changeOrderState(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Integer idOrderDetail);
}
