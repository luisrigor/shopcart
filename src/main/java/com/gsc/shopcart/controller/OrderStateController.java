package com.gsc.shopcart.controller;

import com.google.gson.Gson;
import com.gsc.shopcart.constants.ApiEndpoints;
import com.gsc.shopcart.dto.GetOrderStateDTO;
import com.gsc.shopcart.dto.OrderStateDTO;
import com.gsc.shopcart.repository.scart.OrderDetailRepository;
import com.gsc.shopcart.repository.scart.ProductRepository;
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
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @PostMapping(ApiEndpoints.GET_ORDER_STATE)
    public ResponseEntity<?> getOrderState(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody GetOrderStateDTO getOrderStateDTO) {
        Gson gson = new Gson();
        OrderStateDTO orderStateDTO = orderStateService.getOrderState(userPrincipal,getOrderStateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(orderStateDTO));
    }
/* idOrder 373 & idStatus 3
    @GetMapping(ApiEndpoints.SEND_INVOICE)
    @ResponseBody
    public ResponseEntity<?> testController(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Integer idOrder,
                                            @RequestParam Integer idOrderStatus){
        return ResponseEntity.status(HttpStatus.OK).body(orderDetailRepository.findByIdOrderAndIdOrderStatus(idOrder,idOrderStatus));
    }

 */
    @GetMapping(ApiEndpoints.SEND_INVOICE)
    @ResponseBody
    public ResponseEntity<?> testController(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.getBillToByIdProduct(id));
    }


}
