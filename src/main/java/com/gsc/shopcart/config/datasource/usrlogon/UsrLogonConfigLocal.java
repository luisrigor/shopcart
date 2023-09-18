package com.gsc.shopcart.config.datasource.usrlogon;

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
import org.springframework.context.annotation.Primary;
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
        entityManagerFactoryRef = "usrlogonEntityManagerFactory",
        transactionManagerRef = "msTransactionManager",
        basePackages = {"com.gsc.shopcart.repository.usrlogon"}
)
public class UsrLogonConfigLocal {

    @Autowired
    private Environment env;
    @Value("${sc.config.file}")
    private String scConfigFile;

    @PostConstruct
    private void init() {
        SCGlobalPreferences.setResources(scConfigFile);
        ServerJDBCConnection conn = ServerJDBCConnection.getInstance();
        DB2SimpleDataSource dbToynet = new DB2SimpleDataSource();
        dbToynet.setServerName("scdbesrva.sc.pt");
        dbToynet.setPortNumber(50000);
        dbToynet.setDatabaseName("USRLOGON");
        dbToynet.setDriverType(4);
        dbToynet.setUser("db2inst1");
        dbToynet.setPassword("db2admin");
        conn.setDataSource(dbToynet, "jdbc/usrlogon");
        log.info("Datasource initialized successfully: jdbc/usrlogon");
    }

    @Bean(name="usrlogonDataSource")
    @ConfigurationProperties(prefix = "usrlogon.datasource")
    DataSource dataSource(){
        return DataSourceBuilder.create()
                .url(env.getProperty("app.datasource.second.url"))
                .driverClassName(env.getProperty("app.datasource.second.driver-class-name"))
                .username(env.getProperty("app.datasource.second.username"))
                .password(env.getProperty("app.datasource.second.password"))
                .build();
    }

    @Bean(name = "usrlogonEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean usrlogonEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("usrlogonDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.gsc.shopcart.model.usrlogon.entity")
                .persistenceUnit("usrlogonPersistenceUnit")
                .properties(getHibernateProperties())
                .build();
    }
    @Bean(name = "usrlogonTransactionManager")
    PlatformTransactionManager usrlogonTransactionManager(@Qualifier("usrlogonEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Map<String, Object> getHibernateProperties() {
        Map<String, Object> hibernateProperties = new HashMap<>();
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.DB2Dialect");
        return hibernateProperties;
    }
}
