package com.gsc.shopcart.config.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.function.Predicate;

@Component
@Getter
@Setter
public class EnvironmentProperties {

    public static String LOCAL = "local";
    public static String DEVELOPMENT = "development";
    public static String UAT = "staging";
    public static String PRODUCTION = "production";
    private Environment environment;

    @Autowired
    public EnvironmentProperties(Environment environment) {
        this.environment = environment;
    }

    public boolean isLocalProfile(){
        return Arrays.stream(environment.getActiveProfiles()).anyMatch(contains(LOCAL));
    }

    public boolean isDevelopmentProfile(){
        return Arrays.stream(environment.getActiveProfiles()).anyMatch(contains(DEVELOPMENT));
    }

    public boolean isUATProfile(){
        return Arrays.stream(environment.getActiveProfiles()).anyMatch(contains(UAT));
    }

    public boolean isProductionProfile(){
        return Arrays.stream(environment.getActiveProfiles()).anyMatch(contains(PRODUCTION));
    }

    private Predicate<String> contains(String profile) {
        return profiles -> profiles.contains(profile);
    }
}
