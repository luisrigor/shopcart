package com.gsc.shopcart.config.datasource.usrlogon;

import com.gsc.shopcart.config.properties.EnvironmentProperties;;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Log4j
@Profile(value = {"local","development","staging","production"})
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "usrlogonEntityManagerFactory",
        transactionManagerRef = "usrlogonTransactionManager",
        basePackages = {"com.gsc.shopcart.repository.usrlogon"}
)
public class UsrLogonConfig {

    final Logger logger = LoggerFactory.getLogger(UsrLogonConfig.class);
    private final Environment env;
    private final EnvironmentProperties environmentProperties;


    public UsrLogonConfig(Environment env,EnvironmentProperties environmentProperties) {
        this.env = env;
        this.environmentProperties = environmentProperties;
    }

    @ConfigurationProperties(prefix = "usrlogon.datasource")
    @Bean(name = "usrlogonDataSource", destroyMethod = "")
    public DataSource dataSource() throws NamingException {
        if(environmentProperties.isLocalProfile())
            return DataSourceBuilder.create().build();

        String jndi = env.getProperty("usrlogon.datasource.jndi-name");
        if (jndi == null) {
            return null;
        }
        logger.info("Jndi initialized: " + jndi);
        return (DataSource) new JndiTemplate().lookup(jndi);
    }

    @Bean(name = "usrlogonEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("usrlogonDataSource") DataSource dataSource) {
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
