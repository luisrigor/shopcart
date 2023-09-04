package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.LoginKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginKeyRepository extends JpaRepository<LoginKey, Long> {

    Optional<LoginKey> findFirstByEnabledIsTrue();
}

