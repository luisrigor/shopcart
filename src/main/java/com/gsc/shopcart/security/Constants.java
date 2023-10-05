package com.gsc.shopcart.security;

import java.io.IOException;
import java.util.Properties;
public class Constants {
    private static Properties properties;
    public static String AUTHORIZATION = getMsg("authorization");
    public static String INVALID_TOKEN = getMsg("invalid.token");
    public static String TOKEN_NOT_FOUND = getMsg("token.not.found");
    public static String USER_NOT_FOUND = getMsg("user.not.found");
    public static String USER_AUTHENTICATION_ERROR = getMsg("user.authentication.error");

    private static String getMsg(String key) {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(Thread.currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream("constants.properties"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return properties.getProperty(key);
    }
}
