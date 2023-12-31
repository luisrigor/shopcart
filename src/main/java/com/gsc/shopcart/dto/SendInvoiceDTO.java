package com.gsc.shopcart.dto;


import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendInvoiceDTO {

    private List<Integer> orderList;
}