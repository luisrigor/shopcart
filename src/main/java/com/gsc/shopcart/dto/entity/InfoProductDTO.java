package com.gsc.shopcart.dto.entity;


import com.gsc.shopcart.model.scart.entity.Product;
import com.gsc.shopcart.model.scart.entity.ProductItem;
import com.gsc.shopcart.model.scart.entity.ProductPriceRule;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoProductDTO {

    private Product product;
    private String namespace;
    private String virtualpath;
    private List<ProductItem> productItemList;
    private List<ProductPriceRule> productPriceRules;
    private String idCatalog;
}
