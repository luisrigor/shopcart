package com.gsc.shopcart.config.environment;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class MapProfileVariables {

    public static final String PATH_TO_WRITE_FILES = "PATH_TO_WRITE_FILES";

    public static Map<String, String> getEnvVariablesLocal() {
        Map<String, String> envVariables = new HashMap<>();
        envVariables.put(PATH_TO_WRITE_FILES, "C:\\Windows\\Temp");
        return envVariables;
    }

    public static Map<String, String> getEnvVariablesDevelopment() {
        Map<String, String> envVariables = new HashMap<>();
        envVariables.put(PATH_TO_WRITE_FILES, "/home/www/files/faa");
        return envVariables;
    }

    public static Map<String, String> getEnvVariablesStaging() {
        Map<String, String> envVariables = new HashMap<>();
        envVariables.put(PATH_TO_WRITE_FILES, "/home/www/files/faa");
        return envVariables;
    }

    public static Map<String, String> getEnvVariablesProduction() {
        Map<String, String> envVariables = new HashMap<>();
        envVariables.put(PATH_TO_WRITE_FILES, "/home/www/files/faa");
        return envVariables;
    }

}
