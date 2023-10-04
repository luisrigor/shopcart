package com.gsc.shopcart.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "datasources")
@Getter
@Setter
/**
 * Class that represents the properties of a datasource
 */
public class DataSourcesProperties {

    private List<Datasource> datasourceList;

    @Getter
    @Setter
    public static class Datasource{
        private String username;
        private String password;
        private String url;
        private String driverClassName;
        private String jndiName;
    }

}

