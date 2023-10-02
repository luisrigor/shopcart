package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.ProductAttribute;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class ProductAttributeCustomRepositoryImpl implements ProductAttributeCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void mergeProductAttributes(ProductAttribute oProductAttributess, String createdBy) {

        StringBuilder SQL = new StringBuilder("");

        SQL.append("MERGE INTO PRODUCT_ATTRIBUTES AS PA " );
        SQL.append("USING (VALUES (" + oProductAttributess.getIdProduct() + ")) AS AUX (ID_PRODUCT) ");
        SQL.append("ON PA.ID_PRODUCT = AUX.ID_PRODUCT ");
        SQL.append("WHEN MATCHED THEN ");
        SQL.append("UPDATE SET FIELD1=?1, FIELD2=?2, FIELD3=?3, FIELD4=?4, FIELD5=?5, FIELD6=?6, FIELD7=?7, FIELD8=?8, CHANGED_BY=?9, DT_CHANGED=CURRENT TIMESTAMP ");
        SQL.append("WHEN NOT MATCHED THEN ");
        SQL.append("INSERT (ID_PRODUCT,FIELD1,FIELD2,FIELD3,FIELD4,FIELD5,FIELD6,FIELD7,FIELD8,CREATED_BY) ");
        SQL.append("VALUES (?10,?11,?12,?13,?14,?15,?16,?17,?18,?19)");


        Query query = em.createNativeQuery(SQL.toString());

        query
                .setParameter(1, oProductAttributess.getField1())
                .setParameter(2, oProductAttributess.getField2())
                .setParameter(3, oProductAttributess.getField3())
                .setParameter(4, oProductAttributess.getField4())
                .setParameter(5, oProductAttributess.getField5())
                .setParameter(6, oProductAttributess.getField6())
                .setParameter(7, oProductAttributess.getField7())
                .setParameter(8, oProductAttributess.getField8())
                .setParameter(9, createdBy)
                .setParameter(10, oProductAttributess.getIdProduct())
                .setParameter(11, oProductAttributess.getField1())
                .setParameter(12, oProductAttributess.getField2())
                .setParameter(13, oProductAttributess.getField3())
                .setParameter(14, oProductAttributess.getField4())
                .setParameter(15, oProductAttributess.getField5())
                .setParameter(16, oProductAttributess.getField6())
                .setParameter(17, oProductAttributess.getField7())
                .setParameter(18, oProductAttributess.getField8())
                .setParameter(19, createdBy)
                .executeUpdate();
    }
}
