package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Integer> {

    @Query("SELECT PA FROM ProductAttribute  PA WHERE PA.idProduct = :idProduct ")
    ProductAttribute getProductAttributes(@Param("idProduct") Integer idProduct);
}
