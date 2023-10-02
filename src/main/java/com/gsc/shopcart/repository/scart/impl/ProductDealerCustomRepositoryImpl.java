package com.gsc.shopcart.repository.scart.impl;

import com.gsc.shopcart.model.scart.entity.ProductDealer;
import com.gsc.shopcart.repository.scart.ProductDealerCustomRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ProductDealerCustomRepositoryImpl implements ProductDealerCustomRepository {


    @PersistenceContext
    private EntityManager em;


    @Override
    @Transactional
    public ProductDealer createProductDealer(Integer idProduct, String oidDealerParent) {
        StringBuilder SQL = new StringBuilder();

        SQL.append("MERGE INTO PRODUCT_DEALER AS CP ");
        SQL.append("USING (VALUES (" + idProduct + ",'" + oidDealerParent + "')) AS AUX (ID_PRODUCT,OID_DEALER_PARENT) ");
        SQL.append("ON CP.ID_PRODUCT = AUX.ID_PRODUCT AND CP.OID_DEALER_PARENT = AUX.OID_DEALER_PARENT ");
        SQL.append("WHEN MATCHED THEN ");
        SQL.append("UPDATE SET ID_PRODUCT=" + idProduct + ", OID_DEALER_PARENT='" + oidDealerParent + "' ");
        SQL.append("WHEN NOT MATCHED THEN ");
        SQL.append("INSERT (ID_PRODUCT,OID_DEALER_PARENT) VALUES ");
        SQL.append(" (" + idProduct + ", '" + oidDealerParent + "') ");

        em.createNativeQuery(SQL.toString())
                .executeUpdate();

        return new ProductDealer(idProduct, oidDealerParent);
    }
}
