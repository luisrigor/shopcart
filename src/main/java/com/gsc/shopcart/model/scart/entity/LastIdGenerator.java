package com.gsc.shopcart.model.scart.entity;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class LastIdGenerator implements IdentifierGenerator, Configurable {

    String prefix = "";
    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        prefix = properties.getProperty("entityClassName");
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        String entityClassName = prefix;

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
