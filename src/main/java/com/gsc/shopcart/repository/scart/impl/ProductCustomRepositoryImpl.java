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

    @Override
    public List<Product> getProductsByIdCategory(int idCategory, String view, String userOidDealer) {
        StringBuilder SQL = new StringBuilder("");


        SQL.append(" SELECT P.* FROM PRODUCT P ");
        SQL.append(" LEFT JOIN CATEGORY_PRODUCTS CP ON CP.ID_PRODUCT = P.ID ");
        SQL.append(" WHERE CP.ID_CATEGORY = ?1 ");
        if (view.equalsIgnoreCase("CATALOG")) {
            if (userOidDealer != null) {
                SQL.append(" AND (P.ID IN (SELECT PD.ID_PRODUCT FROM PRODUCT_DEALER PD WHERE PD.OID_DEALER_PARENT = '" + userOidDealer + "') OR P.ID IN (SELECT P1.ID FROM PRODUCT P1 WHERE P1.ID NOT IN (SELECT ID_PRODUCT FROM PRODUCT_DEALER))) ");
            }
            SQL.append(" AND (P.STATUS = 'ACTIVO') AND ");
            SQL.append(" ( ");
            SQL.append(" (P.START_DATE<=CURRENT DATE AND P.END_DATE>=CURRENT_DATE) ");
            SQL.append(" OR ");
            SQL.append(" (P.START_DATE IS NULL AND P.END_DATE>=CURRENT_DATE) ");
            SQL.append(" OR ");
            SQL.append(" (P.END_DATE IS NULL AND P.START_DATE<=CURRENT DATE) ");
            SQL.append(" OR ");
            SQL.append(" (P.START_DATE IS NULL AND P.END_DATE IS NULL) ");
            SQL.append(" ) ");
        }
        SQL.append("ORDER BY P.DISPLAY_ORDER,P.NAME, P.REF ");

        Query query = em.createNativeQuery(SQL.toString(), Product.class);

        List<Product> result = query
                .setParameter(1, idCategory)
                .getResultList();

        return result;
    }
}
