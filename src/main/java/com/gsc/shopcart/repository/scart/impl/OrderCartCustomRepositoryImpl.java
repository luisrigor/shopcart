package com.gsc.shopcart.repository.scart.impl;

import com.gsc.shopcart.dto.entity.OrderCartProduct;
import com.gsc.shopcart.repository.scart.OrderCartCustomRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class OrderCartCustomRepositoryImpl implements OrderCartCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override

    public List<OrderCartProduct> getOrderCartByIdUserAndIdCatalog(Integer idUser, Integer idCatalog) {

        StringBuilder SQL = new StringBuilder("");


        SQL.append("SELECT OC.*,PRODUCT.NAME, PRODUCT.THUMBNAIL_PATH, PRODUCT.UNITPRICE, PRODUCT.IVA_TYPE, " +
                "OC.QUANTITY*PRODUCT.UNITPRICE AS TOTAL, PRODUCT.PRICE_RULES, PRODUCT.PROMO_START, PRODUCT.PROMO_END, PRODUCT.PROMO_PRICE, ");
        SQL.append("(SELECT COUNT(ID) FROM PRODUCT_PROPERTY WHERE PRODUCT_PROPERTY.ID_PRODUCT = OC.ID_PRODUCT) AS NUM_OF_PRODUCT_PROPERTIES ");
        SQL.append("FROM ORDER_CART OC ");
        SQL.append("LEFT JOIN PRODUCT ON PRODUCT.ID = OC.ID_PRODUCT ");
        SQL.append("WHERE ID_USER = ?1 AND ID_CATALOG = ?2 ");

        Query query = em.createNativeQuery(SQL.toString(), "OrderProductMapping");

        List<OrderCartProduct> result = query
                .setParameter(1, idUser)
                .setParameter(2, idCatalog)
                .getResultList();

        return result;

    }
}
