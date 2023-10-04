package com.gsc.shopcart.config.environment;

import com.gsc.scgscwsauthentication.invoke.SCAuthenticationInvoke;
import com.gsc.shopcart.config.properties.DataSourcesProperties;
import com.gsc.shopcart.config.properties.EnvironmentProperties;
import com.sc.commons.initialization.SCGlobalPreferences;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.Map;

@Profile("production")
@Component
public class ProductionEnvironmentConfig extends DataSourcesProperties implements EnvironmentConfig {

   public ProductionEnvironmentConfig() {
      SCGlobalPreferences.setResources("/home/www/config/sc_config.properties");
   }
   @Override
   public SCAuthenticationInvoke getAuthenticationInvoker() {
      return new SCAuthenticationInvoke(com.gsc.scgscwsauthentication.invoke.DATA.URL_PRODUCTION);
   }

   @Override
   public void loadDataSources(){
      DataSourceLoader.loadDataSource(EnvironmentProperties.PRODUCTION, getDatasourceList());
   }
   @Override
   public Map<String, String> getEnvVariables() {
      return MapProfileVariables.getEnvVariablesProduction();
   }




}
