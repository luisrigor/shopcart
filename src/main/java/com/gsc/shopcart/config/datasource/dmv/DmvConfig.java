package com.gsc.shopcart.config.datasource.dmv;

import com.sc.commons.dbconnection.ServerJDBCConnection;
import com.sc.commons.initialization.SCGlobalPreferences;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Log4j
@Profile(value = {"development","staging","production"})
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "carEntityManagerFactory",
        basePackages = {"com.gsc.shopcart.repository.dmv"}
)
public class DmvConfig {

    @Autowired
    private Environment env;

    @Value("${sc.config.file}")
    private String scConfigFile;

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String hibernateDialect;

    @Value("${app.datasource.first.jndi}")
    private String jndi;

    @PostConstruct
    private void init() {
        SCGlobalPreferences.setResources(scConfigFile);
        try {
            InitialContext ctx = new InitialContext();
            ServerJDBCConnection conn = ServerJDBCConnection.getInstance();
            conn.setDataSource((DataSource) ctx.lookup(jndi), jndi);
            log.info("Datasource initialized successfully: : jdbc/dbdmv");
        } catch (NamingException e) {
            log.error("Error initializing datasource ({}): jdbc/dbdmv");
        }
    }

    @Bean(name="carDataSource")
    @ConfigurationProperties(prefix = "carn.datasource")
    DataSource dataSource() throws NamingException {
        log.info("INIT PRIMARY JNDI: " + jndi);
        return (DataSource) new InitialContext().lookup(jndi);
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
    PlatformTransactionManager carTransactionManager(@Qualifier("carEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Map<String, Object> getHibernateProperties() {
        Map<String, Object> hibernateProperties = new HashMap<>();
        hibernateProperties.put("hibernate.dialect", hibernateDialect);
        return hibernateProperties;
    }

}
