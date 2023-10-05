package com.gsc.shopcart.dto.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProdVariantDTO {

    private String ivPath;
    private Integer idCatalog = -1;
    private Integer idProduct = 0;
    private Integer idCategory = 0;
    private Integer idProductVariant = 0;
    private String color = "";
    private String description = "";
    private Integer displayOrder = 9999;
    private String name = "";
    private String size = "";
    private String sku = "";
    private String status = "";
    private Integer stock = 0;
    private Character stockcontrol = 'N';
    private String type = "";

}
