package com.gsc.shopcart.controller.impl;

import com.google.gson.Gson;
import com.gsc.shopcart.controller.OrderStateController;
import com.gsc.shopcart.dto.GetOrderStateDTO;
import com.gsc.shopcart.dto.ListOrderDTO;
import com.gsc.shopcart.dto.OrderStateDTO;
import com.gsc.shopcart.dto.SendInvoiceDTO;
import com.gsc.shopcart.model.scart.entity.OrderDetail;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.service.OrderStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderStateControllerImpl implements OrderStateController {

    private final OrderStateService orderStateService;

    @Override
    public ResponseEntity<?> getOrderState(UserPrincipal userPrincipal,GetOrderStateDTO getOrderStateDTO) {
        Gson gson = new Gson();
        OrderStateDTO orderStateDTO = orderStateService.getOrderState(userPrincipal,getOrderStateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(orderStateDTO));
    }

    @Override
    public ResponseEntity<String> sendInvoice(UserPrincipal userPrincipal,SendInvoiceDTO sendInvoiceDTO){
        orderStateService.sendInvoice(userPrincipal,sendInvoiceDTO.getOrderList());
        return ResponseEntity.status(HttpStatus.OK).body("Send Invoice Successfully Executed");
    }

    @Override
    public ResponseEntity<ListOrderDTO> listOrderDetail(UserPrincipal userPrincipal,Integer idOrder,
                                                        Integer idOrderDetailStatus){
        return ResponseEntity.status(HttpStatus.OK).body(orderStateService.listOrderDetail(userPrincipal,idOrder,idOrderDetailStatus));
    }

    @Override
    public ResponseEntity<OrderDetail> changeOrderState(UserPrincipal userPrincipal,Integer idOrderDetail){
        return ResponseEntity.status(HttpStatus.OK).body(orderStateService.changeOrderDetailStatus(idOrderDetail));
    }
}
