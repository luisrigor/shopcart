package com.gsc.shopcart.repository.scart.impl;

import com.gsc.shopcart.model.scart.entity.Product;
import com.gsc.shopcart.repository.scart.ProductCustomRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Product> getProductsInPromotion(int idRootCategory) {

        StringBuilder SQL = new StringBuilder("");

        SQL.append("WITH TEMP (ID_CATEGORY) AS ( ");
        SQL.append("SELECT C.ID AS ID_CATEGORY ");
        SQL.append("FROM CATEGORY C ");
        SQL.append("WHERE C.ID_PARENT = ?1 ");
        SQL.append("UNION ALL ");
        SQL.append("SELECT CATEGORY.ID AS ID_CATEGORY FROM CATEGORY, TEMP ");
        SQL.append("WHERE TEMP.ID_CATEGORY = CATEGORY.ID_PARENT ");
        SQL.append(") ");
        SQL.append("SELECT * FROM  PRODUCT P ");
        SQL.append("WHERE P.ID IN (SELECT ID_PRODUCT FROM TEMP, CATEGORY_PRODUCTS CP WHERE CP.ID_PRODUCT=P.ID AND CP.ID_CATEGORY = TEMP.ID_CATEGORY) ");
        SQL.append("AND ( ");
        SQL.append("(P.PROMO_START<=CURRENT DATE AND P.PROMO_END>=CURRENT_DATE) ");
        SQL.append("OR ");
        SQL.append("(P.PROMO_START IS NULL AND P.PROMO_END>=CURRENT_DATE) ");
        SQL.append("OR ");
        SQL.append("(P.PROMO_END IS NULL AND P.PROMO_START<=CURRENT DATE) ");
        SQL.append(") ");
        SQL.append("ORDER BY P.DISPLAY_ORDER,P.NAME, P.REF ");

        Query query = em.createNativeQuery(SQL.toString(), Product.class);

        List<Product> result = query
                .setParameter(1, idRootCategory)
                .getResultList();

        return result;
    }
}
