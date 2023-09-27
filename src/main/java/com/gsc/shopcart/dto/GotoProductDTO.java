package com.gsc.shopcart.dto;

import com.gsc.shopcart.model.scart.entity.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GotoProductDTO {

    private Product product = new Product();
    private String idCategory;
    private String idCatalog;
    private List<VecCategoriesDTO> vecCategoriesByRoot = new ArrayList<>();
    private List<ProductItem> vecProductItem = new ArrayList<>();
    private List<ProductPriceRule> vecProductPriceRules = new ArrayList<>();
    private List<ProductPropertyOrder> vecProductPropety = new ArrayList<>();
    private List<ProductVariant> vecProductVariant = new ArrayList<>();
    private List<RelatedProduct> vecRelatedProducts = new ArrayList<>();
    private List<Object[]> suppliers = new ArrayList<>();
    private Hashtable dealers = new Hashtable();
    private List<CatalogAdditionalInfo> vecAditionalInfo = new ArrayList<>();
    private ProductAttribute oProductAttributes = new ProductAttribute();
}
