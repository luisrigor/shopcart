package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.ProductPriceRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductPriceRuleRepository extends JpaRepository<ProductPriceRule, Integer> {

    @Query(" SELECT PPR FROM ProductPriceRule PPR WHERE PPR.idProduct = :idProduct " +
            "ORDER BY PPR.minimumQuantity ASC ")
    List<ProductPriceRule> getProductPriceRules(@Param("idProduct") Integer idProduct);

    @Query(value = "SELECT VALUE(MIN(MINIMUM_QUANTITY), 9999) AS MINIMUM_QUANTITY, UNIT_PRICE " +
            "FROM PRODUCT_PRICE_RULES WHERE ID_PRODUCT = :idProduct " +
            "AND (MINIMUM_QUANTITY <= :quantity OR :quantity < 0) " +
            "GROUP BY UNIT_PRICE ORDER BY MINIMUM_QUANTITY LIMIT 1",nativeQuery = true)
    List<String[]> getMinProductPriceRulesByIdProduct(@Param("idProduct") int idProduct, @Param("quantity") int quantity);



}
