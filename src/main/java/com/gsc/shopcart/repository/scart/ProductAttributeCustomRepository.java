package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.ProductAttribute;

public interface ProductAttributeCustomRepository {

    void mergeProductAttributes(ProductAttribute oProductAttributess, String createdBy);
}
