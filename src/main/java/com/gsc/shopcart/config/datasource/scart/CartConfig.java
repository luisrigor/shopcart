package com.gsc.shopcart.config.datasource.scart;

import com.gsc.shopcart.config.properties.EnvironmentProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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

@Profile(value = {"local","development","staging","production"})
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "shopcartEntityManagerFactory",
        transactionManagerRef = "shopcartTransactionManager",
        basePackages = {"com.gsc.shopcart.repository.scart"}
)
public class CartConfig {

    final Logger logger = LoggerFactory.getLogger(CartConfig.class);
    private final Environment env;
    private final EnvironmentProperties environmentProperties;

    public CartConfig(Environment env,EnvironmentProperties environmentProperties) {
        this.env = env;
        this.environmentProperties = environmentProperties;
    }


    @Primary
    @ConfigurationProperties(prefix = "shopcart.datasource")
    @Bean(name = "shopcartDataSource", destroyMethod = "")
    public DataSource dataSource() throws NamingException {
        if(environmentProperties.isLocalProfile())
            return DataSourceBuilder.create().build();

        String jndi = env.getProperty("shopcart.datasource.jndi-name");
        if (jndi == null) {
            return null;
        }
        logger.info("Jndi initialized: " + jndi);
        return (DataSource) new JndiTemplate().lookup(jndi);
    }

    @Primary
    @Bean(name = "shopcartEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("shopcartDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.gsc.shopcart.model.scart.entity")
                .persistenceUnit("shopcartPersistenceUnit")
                .properties(getHibernateProperties())
                .build();
    }
    @Primary
    @Bean(name = "shopcartTransactionManager")
    PlatformTransactionManager transactionManager(@Qualifier("shopcartEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
    private Map<String, Object> getHibernateProperties() {
        Map<String, Object> hibernateProperties = new HashMap<>();
        hibernateProperties.put("hibernate.dialect",  env.getProperty("shopcart.datasource.dialect"));
        return hibernateProperties;
    }

}
