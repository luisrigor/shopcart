package com.gsc.shopcart.repository.scart.impl;

import com.gsc.shopcart.dto.ProductPropertyInputValue;
import com.gsc.shopcart.dto.ProductPropertyOrder;
import com.gsc.shopcart.exceptions.SQLCustomException;
import com.gsc.shopcart.repository.scart.ProductPropertyCustomRepository;
import com.sc.commons.utils.DataBaseTasks;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<ProductPropertyInputValue> getOrderCartProductProperty(Integer idOrderCart, Integer idProduct, String mandatory) {

        StringBuilder sql = new StringBuilder(StringUtils.EMPTY);

        try {

            sql.append("SELECT PP.*,OCPP.INPUT_VALUE FROM PRODUCT_PROPERTY PP ");
            sql.append("LEFT JOIN ORDER_CART_PRODUCT_PROPERTY OCPP ON OCPP.ID_PRODUCT_PROPERTY = PP.ID AND OCPP.ID_ORDER_CART= :idOrderCart ");
            sql.append("LEFT JOIN ORDER_CART OC ON OC.ID_PRODUCT = PP.ID_PRODUCT ");
            sql.append("WHERE OC.ID= :idOrderCart  AND PP.ID_PRODUCT = :idProduct AND PP.STATUS = 'S' AND PP.MANDATORY LIKE ('" + DataBaseTasks.prepareStringToLike(mandatory) + "')  ORDER BY OCPP.ID,PP.RANK ");


            Query query = em.createNativeQuery(sql.toString(), "ProductPropertyInputValueMapping");
            query.setParameter("idOrderCart",idOrderCart);
            query.setParameter("idProduct",idProduct);

            return query.getResultList();

        }  catch (Exception ex) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("idOrderCart", idOrderCart);
            parameters.put("idProduct", idProduct);
            parameters.put("mandatory", mandatory);
            throw new SQLCustomException(String.valueOf(sql), parameters, ex);
        }
    }
}
