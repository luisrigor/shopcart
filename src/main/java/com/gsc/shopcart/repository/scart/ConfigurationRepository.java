package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.AuthConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<AuthConfiguration, Long> {

    final String LOGIN_ENABLED = "loginEnabled";
    final String KEY_CREATION = "keyCreationStatus";
    final String TOKEN_EXPIRATION_MILISECONDS = "tokenExpirationMiliseconds";

    AuthConfiguration findByName(String name);

    default Boolean isLoginEnabled() {
        return Boolean.valueOf(findByName(LOGIN_ENABLED).getValue());
    }

    default Boolean isKeyCreationEnabled() {
        return Boolean.valueOf(findByName(KEY_CREATION).getValue());
    }

    default Long getTokenExpirationMsec() {
        return Long.valueOf(findByName(TOKEN_EXPIRATION_MILISECONDS).getValue());
    }
}
