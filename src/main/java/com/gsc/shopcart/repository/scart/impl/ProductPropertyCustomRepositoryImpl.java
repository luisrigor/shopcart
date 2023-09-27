package com.gsc.shopcart.repository.scart.impl;

import com.gsc.shopcart.dto.ProductPropertyOrder;
import com.gsc.shopcart.dto.RelatedProduct;
import com.gsc.shopcart.repository.scart.ProductPropertyCustomRepository;
import com.sc.commons.utils.DataBaseTasks;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class ProductPropertyCustomRepositoryImpl implements ProductPropertyCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ProductPropertyOrder> getProductPropertyByIdProduct(Integer idProduct, String status) {

        StringBuilder SQL = new StringBuilder("");


        SQL.append("SELECT PP.*, ");
        SQL.append("(SELECT COUNT(ID) AS HAS_PROPERTIES_IN_ORDER_CART FROM ORDER_CART_PRODUCT_PROPERTY WHERE ORDER_CART_PRODUCT_PROPERTY.ID_PRODUCT_PROPERTY = PP.ID), ");
        SQL.append("(SELECT COUNT(ID) AS HAS_PROPERTIES_IN_ORDER_DETAIL FROM ORDER_DETAIL_PRODUCT_PROPERTY WHERE ORDER_DETAIL_PRODUCT_PROPERTY.ID_PRODUCT_PROPERTY = PP.ID) ");
        SQL.append("FROM PRODUCT_PROPERTY PP ");
        SQL.append("WHERE PP.ID_PRODUCT = ? AND PP.STATUS LIKE '" + DataBaseTasks.prepareStringToLike(status) + "' ORDER BY PP.RANK ");


        Query query = em.createNativeQuery(SQL.toString(), "ProductPropertyOrderMapping");

        List<ProductPropertyOrder> result = query
                .setParameter(1, idProduct)
                .getResultList();

        return result;


    }
}
