package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductItemRepository extends JpaRepository<ProductItem, Integer> {

    @Query("SELECT PI FROM ProductItem  PI WHERE PI.idProduct = :idProduct ")
    List<ProductItem> getProductItemByIdProduct(@Param("idProduct") Integer idProduct);
}
