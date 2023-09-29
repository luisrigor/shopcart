package com.gsc.shopcart.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    private Integer idProduct;
    private List<String> idsCategory;
}
