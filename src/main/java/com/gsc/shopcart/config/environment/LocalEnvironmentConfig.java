package com.gsc.shopcart.config.environment;

import com.gsc.scgscwsauthentication.invoke.SCAuthenticationInvoke;
import com.gsc.shopcart.config.properties.DataSourcesProperties;
import com.gsc.shopcart.config.properties.EnvironmentProperties;
import com.sc.commons.initialization.SCGlobalPreferences;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.Map;

@Profile("local")
@Component
public class LocalEnvironmentConfig extends DataSourcesProperties implements EnvironmentConfig {

   public LocalEnvironmentConfig() {
      SCGlobalPreferences.setResources("C:/Users/Luis Parra /OneDrive - Rigor/Documentos/REPOSITORIOS/Proyectos GITHUB/shopcart/src/main/resources/sc_config.properties");
   }
   @Override
   public SCAuthenticationInvoke getAuthenticationInvoker() {
      return new SCAuthenticationInvoke(com.gsc.scgscwsauthentication.invoke.DATA.URL_STAGING);
   }
   @Override
   public void loadDataSources(){
      DataSourceLoader.loadDataSource(EnvironmentProperties.LOCAL, getDatasourceList());
   }

   @Override
   public Map<String, String> getEnvVariables() {
      return MapProfileVariables.getEnvVariablesLocal();
   }




}
