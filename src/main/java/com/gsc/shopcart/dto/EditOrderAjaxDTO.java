package com.gsc.shopcart.dto;

import com.gsc.shopcart.model.scart.entity.OrderCart;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditOrderAjaxDTO {

    private List<OrderCart> vecOrderCart;
    private Integer qtdToOrder;

}
