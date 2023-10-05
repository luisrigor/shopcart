package com.gsc.shopcart.dto.entity;

import com.gsc.shopcart.model.scart.entity.Category;
import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveCategoryDTO {


    private Integer id;
    private Integer idCategory;
    private Integer idCatalog;
    private String name;
    private String description;
    private String status;
    private String ivPath;
    private Integer displayOrder;
    private List<Category> listCategorySelected;
}
