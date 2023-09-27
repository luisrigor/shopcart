package com.gsc.shopcart.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VecCategoriesDTO {

    private String idCategory;
    private String path;
    private String selected;

}
