package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.dto.VecCategoriesDTO;
import java.util.List;

public interface CategoryCustomRepository {

    void createCategoryProduct(int idCategory, int idProduct, String createdBy);
    List<VecCategoriesDTO> getCategoriesByIdRootCategoryAndIdProductParent(Integer idRootCategory, Integer idProduct);

}
