package com.gsc.shopcart.config.environment;

import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class MapProfileVariables {

    public static Map<String, String> getEnvVariablesLocal() {
        Map<String, String> envVariables = new HashMap<>();
        return envVariables;
    }

    public static Map<String, String> getEnvVariablesDevelopment() {
        Map<String, String> envVariables = new HashMap<>();
        return envVariables;
    }

    public static Map<String, String> getEnvVariablesStaging() {
        Map<String, String> envVariables = new HashMap<>();
        return envVariables;
    }

    public static Map<String, String> getEnvVariablesProduction() {
        Map<String, String> envVariables = new HashMap<>();
        return envVariables;
    }

}
