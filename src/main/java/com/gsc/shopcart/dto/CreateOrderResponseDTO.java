package com.gsc.shopcart.dto;

import com.gsc.shopcart.model.scart.entity.Category;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderResponseDTO {

    private String message;
    private String actionParameter;
    private Integer idCategory;
    private List<Category> listCategorySelected;

}
