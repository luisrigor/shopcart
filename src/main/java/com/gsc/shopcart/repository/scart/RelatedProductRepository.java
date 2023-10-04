package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.RelatedProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import javax.transaction.Transactional;

public interface RelatedProductRepository extends JpaRepository<RelatedProducts, Integer> {

    @Modifying
    @Transactional
    @Query("delete from RelatedProducts R where R.idProduct1=:idProduct1")
    void deleteRelatedProductsByIdProduct1(@Param("idProduct1") Integer idProduct1);
}
