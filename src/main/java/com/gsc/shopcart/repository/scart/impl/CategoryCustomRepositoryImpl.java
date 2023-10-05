package com.gsc.shopcart.repository.scart.impl;

import com.gsc.shopcart.dto.entity.VecCategoriesDTO;
import com.gsc.shopcart.repository.scart.CategoryCustomRepository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void createCategoryProduct(int idCategory, int idProduct, String createdBy) {
        StringBuilder SQL = new StringBuilder();


        SQL.append("MERGE INTO CATEGORY_PRODUCTS AS CP ");
        SQL.append("USING (VALUES (" + idCategory + ","+ idProduct + ")) AS AUX (ID_CATEGORY,ID_PRODUCT) ");
        SQL.append("ON CP.ID_CATEGORY = AUX.ID_CATEGORY AND CP.ID_PRODUCT = AUX.ID_PRODUCT ");
        SQL.append("WHEN MATCHED THEN ");
        SQL.append("UPDATE SET ID_CATEGORY="+ idCategory+ ", ID_PRODUCT="+ idProduct+ " ");
        SQL.append("WHEN NOT MATCHED THEN ");
        SQL.append("INSERT (ID_CATEGORY,ID_PRODUCT,CREATED_BY) VALUES ");
        SQL.append(" (" + idCategory + ", " + idProduct + ", '" + createdBy + "') ");

       em.createNativeQuery(SQL.toString())
                .executeUpdate();
}
  
    public List<VecCategoriesDTO> getCategoriesByIdRootCategoryAndIdProductParent(Integer idRootCategory, Integer idProduct) {

        StringBuilder SQL = new StringBuilder("");


        SQL.append("WITH TEMP (ID_CATEGORY, PATH) AS ( ");
        SQL.append("SELECT C.ID, CAST(C.PATH AS VARCHAR(3500)) ");
        SQL.append("FROM CATEGORY C ");
        SQL.append("WHERE C.ID_PARENT = ?1 ");
        SQL.append("UNION ALL ");
        SQL.append("SELECT CATEGORY.ID, TEMP.PATH || ' / ' || CATEGORY.NAME AS NAME ");
        SQL.append("FROM CATEGORY, TEMP ");
        SQL.append("WHERE TEMP.ID_CATEGORY = CATEGORY.ID_PARENT ");
        SQL.append(") ");
        SQL.append("SELECT T.ID_CATEGORY, T.PATH, (CASE WHEN CP.ID_PRODUCT IS NULL THEN 0 ELSE 1 END) AS SELECTED ");
        SQL.append("FROM TEMP T  ");
        SQL.append("LEFT JOIN CATEGORY_PRODUCTS CP ON T.ID_CATEGORY = CP.ID_CATEGORY AND CP.ID_PRODUCT = ?2 ");
        SQL.append("ORDER BY T.PATH ");

        Query query = em.createNativeQuery(SQL.toString(), "VecCategoriesMapping");

        List<VecCategoriesDTO> result = query
                .setParameter(1, idRootCategory)
                .setParameter(2, idProduct)
                .getResultList();

        return result;
    }
}
