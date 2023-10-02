package com.gsc.shopcart.model.scart.entity;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LastIdGenerator implements IdentifierGenerator {



    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        String entityClassName = "PRODUCT_PRICE_RULES";

        Connection connection = session.connection();
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT MAX(id) FROM " + entityClassName;
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                Integer maxId = rs.getInt(1);
                return maxId + 1;
            }
        } catch (Exception e) {
            throw new HibernateException("Unable to generate ID", e);
        }
        return null;
    }
}
