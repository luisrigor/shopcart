package com.gsc.shopcart.config.datasource.dmv;

import com.gsc.shopcart.config.properties.DataSourcesProperties;
import com.gsc.shopcart.config.properties.EnvironmentProperties;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.boot.jdbc.DataSourceBuilder;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import org.springframework.jndi.JndiTemplate;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Log4j
@Profile(value = {"local","development","staging","production"})
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "dmvEntityManagerFactory",
        transactionManagerRef = "dmvTransactionManager",
        basePackages = {"com.gsc.shopcart.repository.dmv"}
)
public class DmvConfig extends DataSourcesProperties {
    final Logger logger = LoggerFactory.getLogger(DmvConfig.class);
    private final Environment env;
    private final  EnvironmentProperties environmentProperties;

    public DmvConfig(Environment env,EnvironmentProperties environmentProperties) {
        this.env = env;
        this.environmentProperties = environmentProperties;
    }

    @ConfigurationProperties(prefix = "dbdmv.datasource")
    @Bean(name = "dbdmvDatasource", destroyMethod = "")
    public DataSource dataSource() throws NamingException {
        if(environmentProperties.isLocalProfile())
            return DataSourceBuilder.create().build();

        String jndi = env.getProperty("dbdmv.datasource.jndi-name");
        if (jndi == null) {
            return null;
        }
        logger.info("Jndi initialized: " + jndi);
        return (DataSource) new JndiTemplate().lookup(jndi);
    }

    @Bean(name = "dmvEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("dbdmvDatasource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.gsc.shopcart.model.dmv.entity")
                .persistenceUnit("dmvPersistenceUnit")
                .properties(getHibernateProperties())
                .build();
    }

    @Bean(name = "dmvTransactionManager")
    PlatformTransactionManager transactionManager(@Qualifier("dmvEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Map<String, Object> getHibernateProperties() {
        Map<String, Object> hibernateProperties = new HashMap<>();
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.DB2Dialect");
        return hibernateProperties;
    }

}
