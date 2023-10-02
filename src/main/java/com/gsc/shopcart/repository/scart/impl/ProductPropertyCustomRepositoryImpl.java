package com.gsc.shopcart.repository.scart.impl;

import com.gsc.shopcart.model.scart.entity.Product;
import com.gsc.shopcart.repository.scart.ProductPropertyCustomRepository;
import com.gsc.shopcart.security.UserPrincipal;
import com.sc.commons.utils.DataBaseTasks;
import org.springframework.transaction.annotation.Transactional;
import com.gsc.shopcart.dto.ProductPropertyOrder;
import com.gsc.shopcart.dto.RelatedProduct;
import com.gsc.shopcart.repository.scart.ProductPropertyCustomRepository;
import com.sc.commons.utils.DataBaseTasks;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.List;

public class ProductPropertyCustomRepositoryImpl implements ProductPropertyCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public String mergeProductProperty(Integer id, Integer idProduct, String label, String option_value,
                                       Integer max_lenght, String data_type, Integer rank, String help,
                                       String status, String mandatory, String user) {

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        StringBuilder SQL = new StringBuilder();

        SQL.append("MERGE INTO PRODUCT_PROPERTY AS TAB ");
        SQL.append("USING (VALUES (" + id + ")) AS AUX (ID) ON TAB.ID = AUX.ID ");
        SQL.append("WHEN MATCHED THEN ");
        SQL.append("UPDATE SET LABEL = ?1, OPTION_VALUE=?2, MAX_LENGHT=?3, DATA_TYPE=?4, RANK=?5, HELP=?6, STATUS=?7, MANDATORY=?8, CHANGED_BY=?9, DT_CHANGED=?10 ");
        SQL.append("WHEN NOT MATCHED THEN ");
        SQL.append("INSERT (ID_PRODUCT,LABEL,OPTION_VALUE,MAX_LENGHT,DATA_TYPE,RANK,HELP,STATUS,MANDATORY,CREATED_BY,DT_CREATED) VALUES (?11,?12,?13,?14,?15,?16,?17,?18,?19,?20,?21) ");

        Query query = em.createNativeQuery(SQL.toString());

        query
                .setParameter(1, label)
                .setParameter(2, option_value)
                .setParameter(3, max_lenght)
                .setParameter(4, data_type)
                .setParameter(5, rank)
                .setParameter(6, help)
                .setParameter(7, status)
                .setParameter(8, mandatory)
                .setParameter(9,  user)
                .setParameter(10, ts)
                .setParameter(11, idProduct)
                .setParameter(12, label)
                .setParameter(13, option_value)
                .setParameter(14, max_lenght)
                .setParameter(15, data_type)
                .setParameter(16, rank)
                .setParameter(17, help)
                .setParameter(18, status)
                .setParameter(19, mandatory)
                .setParameter(20, user)
                .setParameter(21, ts)
                .executeUpdate();


        SQL = new StringBuilder("SELECT ID FROM PRODUCT_PROPERTY WHERE DT_CREATED = ?1 OR DT_CHANGED = ?2");

        query = em.createNativeQuery(SQL.toString());

        Integer idInt;
        try {
            idInt  = (Integer) query
                    .setParameter(1, ts)
                    .setParameter(2, ts)
                    .getSingleResult();

        } catch (Exception e) {
            return "-1";
        }

        return idInt !=null ?String.valueOf(idInt): "-1";
    }
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
