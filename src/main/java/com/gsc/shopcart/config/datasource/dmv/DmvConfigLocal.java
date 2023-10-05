package com.gsc.shopcart.config.datasource.dmv;

import com.ibm.db2.jcc.DB2SimpleDataSource;
import com.sc.commons.dbconnection.ServerJDBCConnection;
import com.sc.commons.initialization.SCGlobalPreferences;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Log4j
@Profile(value = {"local"} )
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "carEntityManagerFactory",
        transactionManagerRef = "msTransactionManager",
        basePackages = {"com.gsc.shopcart.repository.dmv"}
)
public class DmvConfigLocal {


    @Autowired
    private Environment env;

    @Value("${sc.config.file}")
    private String scConfigFile;



    @PostConstruct
    private void init() {
        SCGlobalPreferences.setResources(scConfigFile);
        ServerJDBCConnection conn = ServerJDBCConnection.getInstance();
        DB2SimpleDataSource dbDmv = new DB2SimpleDataSource();
        dbDmv.setServerName("scdbesrva.sc.pt");
        dbDmv.setPortNumber(50000);
        dbDmv.setDatabaseName("DBDMV");
        dbDmv.setDriverType(4);
        dbDmv.setUser("db2inst1");
        dbDmv.setPassword("db2admin");
        conn.setDataSource(dbDmv, "jdbc/dbdmv");
        log.info("Datasource initialized successfully: jdbc/dbdmv");
    }

    @Bean(name="carDataSource")
    @ConfigurationProperties(prefix = "car.datasource")
    DataSource dataSource(){
        return DataSourceBuilder.create()
                .url(env.getProperty("app.datasource.first.url"))
                .driverClassName(env.getProperty("app.datasource.first.driver-class-name"))
                .username(env.getProperty("app.datasource.first.username"))
                .password(env.getProperty("app.datasource.first.password"))
                .build();
    }

    @Bean(name = "carEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean carEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("carDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.gsc.shopcart.model.dmv.entity")
                .persistenceUnit("carPersistenceUnit")
                .properties(getHibernateProperties())
                .build();
    }

    @Bean(name = "carTransactionManager")
    PlatformTransactionManager caryTransactionManager(@Qualifier("carEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Map<String, Object> getHibernateProperties() {
        Map<String, Object> hibernateProperties = new HashMap<>();
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.DB2Dialect");
        return hibernateProperties;
    }

}
