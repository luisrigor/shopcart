package com.gsc.shopcart.dto.entity;

import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.model.scart.entity.OrderCart;
import com.gsc.shopcart.model.scart.entity.Product;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDTO {
    private Integer idCategory;

    private List<Category> listCategorySelected;
    private List<Category> vecCategories;
    private List<Product> vecProducts;
    private List<OrderCart> vecOrderCart;
    private String virtualPath;
    private String idCatalog;
    private String view;
}
