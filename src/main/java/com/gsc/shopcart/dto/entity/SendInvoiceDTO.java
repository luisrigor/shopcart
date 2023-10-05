package com.gsc.shopcart.dto.entity;


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