package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.dto.VecCategoriesDTO;

import java.util.List;

public interface CategoryCustomRepository {

    List<VecCategoriesDTO> getCategoriesByIdRootCategoryAndIdProductParent(Integer idRootCategory, Integer idProduct);
}
