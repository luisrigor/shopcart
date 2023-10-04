package com.gsc.shopcart.config.environment;

import com.gsc.scgscwsauthentication.invoke.SCAuthenticationInvoke;
import com.gsc.shopcart.config.properties.DataSourcesProperties;
import com.gsc.shopcart.config.properties.EnvironmentProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.Map;
import com.sc.commons.initialization.SCGlobalPreferences;


@Profile("development")
@Component
public class DevelopmentEnvironmentConfig extends DataSourcesProperties implements EnvironmentConfig {

   public DevelopmentEnvironmentConfig() {
      SCGlobalPreferences.setResources("/home/www/config/sc_config.properties");
   }

   @Override
   public SCAuthenticationInvoke getAuthenticationInvoker() {
      return new SCAuthenticationInvoke(com.gsc.scgscwsauthentication.invoke.DATA.URL_STAGING);
   }

   @Override
   public void loadDataSources(){
      DataSourceLoader.loadDataSource(EnvironmentProperties.DEVELOPMENT, getDatasourceList());
   }

   @Override
   public Map<String, String> getEnvVariables() {
      return MapProfileVariables.getEnvVariablesDevelopment();
   }

}
