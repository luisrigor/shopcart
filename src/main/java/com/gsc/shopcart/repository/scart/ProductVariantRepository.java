package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {


    @Query("SELECT PV FROM ProductVariant PV " +
            "LEFT JOIN Product  P ON P.id = PV.idProduct " +
            "WHERE P.id = :idProduct AND UPPER(PV.status) LIKE  '%%%' " +
            "ORDER BY PV.displayOrder, PV.name ")
    List<ProductVariant> getProductVariant(@Param("idProduct") Integer idProduct);
}
