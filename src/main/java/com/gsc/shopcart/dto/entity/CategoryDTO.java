package com.gsc.shopcart.dto.entity;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    private Integer idProduct;
    private List<String> ids;
}
