package com.gsc.shopcart.repository.scart.impl;

import com.gsc.shopcart.model.scart.entity.Product;
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
}
