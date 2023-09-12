package com.gsc.shopcart.dto;

import com.gsc.shopcart.model.scart.entity.Product;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionsDTO {

   private List<Product> vecProducts;
   private String view;
   private String idCategory;
   private String viewOnlyPromotions;
}
