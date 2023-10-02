package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.ProductPriceRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductPriceRuleRepository extends JpaRepository<ProductPriceRule, Integer> {

    @Query(" SELECT PPR FROM ProductPriceRule PPR WHERE PPR.idProduct = :idProduct " +
            "ORDER BY PPR.minimumQuantity ASC ")
    List<ProductPriceRule> getProductPriceRules(@Param("idProduct") Integer idProduct);

    @Query("DELETE FROM ProductPriceRule PPR WHERE PPR.idProduct = :idProduct ")
    @Modifying
    void deleteProductPriceRulesByIdProduct(@Param("idProduct") Integer idProduct);

    @Query("SELECT MAX(p.id) FROM ProductPriceRule p")
    Integer findMaxId();
}
