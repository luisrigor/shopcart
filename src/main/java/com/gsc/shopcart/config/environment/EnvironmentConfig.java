package com.gsc.shopcart.config.environment;


import com.gsc.scgscwsauthentication.invoke.SCAuthenticationInvoke;
import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import java.util.Map;

public interface EnvironmentConfig {

    SCAuthenticationInvoke getAuthenticationInvoker();

    @PostConstruct
    void loadDataSources() throws NamingException;

    Map<String, String> getEnvVariables();


}
