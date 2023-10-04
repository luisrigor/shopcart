package com.gsc.shopcart.config.environment;

import com.gsc.shopcart.config.properties.DataSourcesProperties;
import com.sc.commons.dbconnection.ServerJDBCConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jndi.JndiObjectFactoryBean;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.List;

public class DataSourceLoader {
    static final Logger logger = LoggerFactory.getLogger(DataSourceLoader.class);

    public static void loadDataSource(String env, List<DataSourcesProperties.Datasource> datasourceList){
        logger.info("load datasource for environment:" + env);
        switch (env){
            case "production":
            case "staging":
            case "development":
                loadNotLocalDataSources(datasourceList);
                break;
            case "local":
                loadLocalDataSource(datasourceList);
                break;
        }
    }

    private static void loadLocalDataSource(List<DataSourcesProperties.Datasource> datasourceList) {
        logger.info("### loadLocalDataSource");
        datasourceList.forEach(datasource -> {
            ServerJDBCConnection conn = ServerJDBCConnection.getInstance();
            DataSource dataSource = DataSourceBuilder
                    .create()
                    .username(datasource.getUsername())
                    .password(datasource.getPassword())
                    .url(datasource.getUrl())
                    .driverClassName(datasource.getDriverClassName())
                    .build();

            conn.setDataSource(dataSource, datasource.getJndiName());

            logger.info("DATASOURCE "+datasource.getJndiName()+" INITIALIZED!");
        });
    }

    private static void loadNotLocalDataSources(List<DataSourcesProperties.Datasource> datasourceList) {
        logger.info("### loadNotLocalDataSources");
        datasourceList.forEach(dataSource -> {
            ServerJDBCConnection conn = ServerJDBCConnection.getInstance();
            JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
            jndiObjectFactoryBean.setJndiName(dataSource.getJndiName());
            try {
                jndiObjectFactoryBean.afterPropertiesSet();
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
            conn.setDataSource((DataSource) jndiObjectFactoryBean.getObject(), dataSource.getJndiName());
            logger.info("DATASOURCE "+dataSource.getJndiName()+" INITIALIZED!");
        });
    }

}

