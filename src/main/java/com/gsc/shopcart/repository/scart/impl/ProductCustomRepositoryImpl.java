package com.gsc.shopcart.repository.scart.impl;


import com.gsc.shopcart.dto.entity.RelatedProduct;
import com.gsc.shopcart.dto.entity.ShopCartFilter;
import com.gsc.shopcart.model.scart.entity.Product;
import com.gsc.shopcart.repository.scart.ProductCustomRepository;
import com.sc.commons.utils.DataBaseTasks;
import com.sc.commons.utils.StringTasks;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
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

    @Override
    public List<Product> getProductsByFreeSearch(int idRootCategory, String view, String userOidDealer, ShopCartFilter filter) {

        StringBuilder SQL = new StringBuilder("");

        SQL.append("WITH TEMP1 (ID_CATEGORY) AS ( ");
        SQL.append("SELECT C.ID AS ID_CATEGORY ");
        SQL.append("FROM CATEGORY C ");
        SQL.append("WHERE C.ID_PARENT = ?1 ");
        if (filter.getState().equals("A")) {
            SQL.append(" AND (C.STATUS = 'ACTIVO') ");
        } else if (filter.getState().equals("I")) {
            SQL.append(" AND (C.STATUS = 'INACTIVO') ");
        }
        SQL.append("UNION ALL ");
        SQL.append("SELECT CATEGORY.ID AS ID_CATEGORY FROM CATEGORY, TEMP1 ");
        SQL.append("WHERE TEMP1.ID_CATEGORY = CATEGORY.ID_PARENT ");
        if (filter.getState().equals("A")) {
            SQL.append(" AND (CATEGORY.STATUS = 'ACTIVO') ");
        } else if (filter.getState().equals("I")) {
            SQL.append(" AND (CATEGORY.STATUS = 'INACTIVO') ");
        }
        SQL.append("), ");
        SQL.append("TEMP2 (ID_CATEGORY) AS ( ");
        SQL.append("SELECT C.ID AS ID_CATEGORY ");
        SQL.append("FROM CATEGORY C ");
        SQL.append("WHERE C.ID_PARENT = ?2 ");
        SQL.append("UNION ALL ");
        SQL.append("SELECT CATEGORY.ID AS ID_CATEGORY FROM CATEGORY, TEMP2 ");
        SQL.append("WHERE TEMP2.ID_CATEGORY = CATEGORY.ID_PARENT ");
        SQL.append(") ");
        SQL.append("SELECT P.* FROM PRODUCT P ");
        SQL.append(" LEFT JOIN PRODUCT_VARIANT PV ON PV.ID_PRODUCT = P.ID ");
        SQL.append("WHERE P.ID IN (SELECT ID_PRODUCT FROM TEMP1, CATEGORY_PRODUCTS CP WHERE CP.ID_PRODUCT=P.ID AND CP.ID_CATEGORY = TEMP1.ID_CATEGORY UNION SELECT ID_PRODUCT FROM TEMP2, CATEGORY_PRODUCTS CP WHERE CP.ID_PRODUCT=P.ID AND CP.ID_CATEGORY = TEMP2.ID_CATEGORY) ");
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
        } else {
            if (filter.getState().equals("A")) {
                SQL.append(" AND (P.STATUS = 'ACTIVO') ");
            } else if (filter.getState().equals("I")) {
                SQL.append(" AND (P.STATUS = 'INACTIVO') ");
            }
        }
        SQL.append("AND ( ");
        String cleanFreeSearch = StringTasks.ReplaceSpecialChar(filter.getFreeSearch());
        cleanFreeSearch = StringTasks.ReplaceStr(cleanFreeSearch, "'", "''");
        SQL.append(" VARCHAR(UPPER(P.REF),45) LIKE '" + DataBaseTasks.prepareStringToLike(filter.getFreeSearch().toUpperCase()) + "' OR ");
        SQL.append(" COLLATION_KEY_BIT(P.NAME, 'UCA500R1_S1') = COLLATION_KEY_BIT('" + cleanFreeSearch + "', 'UCA500R1_S1') OR ");
        SQL.append(" VARCHAR(UPPER(P.NAME),90) LIKE '" + DataBaseTasks.prepareStringToLike(filter.getFreeSearch().toUpperCase()) + "' OR ");
        SQL.append(" VARCHAR(UPPER(P.NAME),90) LIKE '" + DataBaseTasks.prepareStringToLike(StringTasks.ReplaceSpecialChar(filter.getFreeSearch()).toUpperCase()) + "' OR ");
        SQL.append(" VARCHAR(UPPER(P.KEYWORDS),90) LIKE '" + DataBaseTasks.prepareStringToLike(filter.getFreeSearch().toUpperCase()) + "' OR ");
        SQL.append(" COLLATION_KEY_BIT(PV.SKU, 'UCA500R1_S1') = COLLATION_KEY_BIT('" + cleanFreeSearch + "', 'UCA500R1_S1') OR ");
        SQL.append(" VARCHAR(UPPER(PV.SKU),18) LIKE '" + DataBaseTasks.prepareStringToLike(filter.getFreeSearch().toUpperCase()) + "' OR ");
        SQL.append(" VARCHAR(UPPER(PV.SKU),18) LIKE '" + DataBaseTasks.prepareStringToLike(StringTasks.ReplaceSpecialChar(filter.getFreeSearch()).toUpperCase()) + "' OR ");
        SQL.append(" COLLATION_KEY_BIT(PV.NAME, 'UCA500R1_S1') = COLLATION_KEY_BIT('" + cleanFreeSearch + "', 'UCA500R1_S1') OR ");
        SQL.append(" VARCHAR(UPPER(PV.NAME),90) LIKE '" + DataBaseTasks.prepareStringToLike(filter.getFreeSearch().toUpperCase()) + "' OR ");
        SQL.append(" VARCHAR(UPPER(PV.NAME),90) LIKE '" + DataBaseTasks.prepareStringToLike(StringTasks.ReplaceSpecialChar(filter.getFreeSearch()).toUpperCase()) + "' ");
        SQL.append(") ");
        SQL.append("ORDER BY P.DISPLAY_ORDER,COLLATION_KEY_BIT(UPPER(P.NAME), 'UCA500R1_S1') ASC ");

        Query query = em.createNativeQuery(SQL.toString(), Product.class);

        List<Product> result = query
                .setParameter(1, idRootCategory)
                .setParameter(2, idRootCategory)
                .getResultList();

        return result;
    }

    @Override
    public List<RelatedProduct> getRelatedProducts(Integer idRootCategory, Integer idProduct) {

         StringBuilder SQL = new StringBuilder("");


         SQL.append("WITH TEMP (ID_CATEGORY, ID_PRODUCT, REF, PRODUCT_NAME) AS ( ");
         SQL.append("SELECT C.ID AS ID_CATEGORY, P.ID AS ID_PRODUCT, P.REF, P.NAME AS PRODUCT_NAME ");
         SQL.append("FROM CATEGORY C, PRODUCT P, CATEGORY_PRODUCTS CP ");
         SQL.append("WHERE C.ID_PARENT = ?1 AND P.ID=CP.ID_PRODUCT AND C.ID=CP.ID_CATEGORY ");
         SQL.append("UNION ALL ");
         SQL.append("SELECT CATEGORY.ID AS ID_CATEGORY, PRODUCT.ID AS ID_PRODUCT, PRODUCT.REF, PRODUCT.NAME AS PRODUCT_NAME ");
         SQL.append("FROM CATEGORY, TEMP, PRODUCT, CATEGORY_PRODUCTS ");
         SQL.append("WHERE TEMP.ID_CATEGORY = CATEGORY.ID_PARENT  AND PRODUCT.ID=CATEGORY_PRODUCTS.ID_PRODUCT AND CATEGORY.ID=CATEGORY_PRODUCTS.ID_CATEGORY ");
         SQL.append(") ");
         SQL.append("SELECT DISTINCT T.ID_PRODUCT, T.REF, T.PRODUCT_NAME, CASE WHEN (SELECT ID_PRODUCT1 FROM RELATED_PRODUCTS WHERE ID_PRODUCT2=?2 AND ID_PRODUCT1=T.ID_PRODUCT)>0 OR (SELECT ID_PRODUCT2 FROM RELATED_PRODUCTS WHERE ID_PRODUCT1=?3 AND ID_PRODUCT2=T.ID_PRODUCT)>0 THEN 'S' ELSE 'N' END AS IS_RELATED_PRODUCT ");
         SQL.append("FROM TEMP T ");
         SQL.append("WHERE T.ID_PRODUCT!=?4 ");
         SQL.append("ORDER BY T.PRODUCT_NAME, T.REF ");


         Query query = em.createNativeQuery(SQL.toString(), "RelatedProductMapping");

         List<RelatedProduct> result = query
                 .setParameter(1, idRootCategory)
                 .setParameter(2, idProduct)
                 .setParameter(3, idProduct)
                 .setParameter(4, idProduct)
                 .getResultList();

         return result;

    }

    @Override
    @Transactional
    public void mergeRelatedProducts(int idProduct1, int idProduct2, String changedBy) {
        StringBuilder SQL = new StringBuilder();


        SQL.append("MERGE INTO RELATED_PRODUCTS AS RP ");
        SQL.append("USING (VALUES (" + idProduct1 + ","+ idProduct2 + ")) AS AUX (ID_PRODUCT1,ID_PRODUCT2) ");
        SQL.append("ON RP.ID_PRODUCT1 = AUX.ID_PRODUCT2 ");
        SQL.append("WHEN MATCHED THEN ");
        SQL.append("UPDATE SET CHANGED_BY=?, DT_CHANGED=CURRENT TIMESTAMP ");
        SQL.append("WHEN NOT MATCHED THEN ");
        SQL.append("INSERT (ID_PRODUCT1,ID_PRODUCT2,CHANGED_BY) VALUES ");
        SQL.append(" (" + idProduct1 + ", " + idProduct2 + ", '" + changedBy + "') ");

        em.createNativeQuery(SQL.toString())
                .executeUpdate();
    }


}
