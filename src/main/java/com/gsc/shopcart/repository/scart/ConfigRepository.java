package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Configuration, String> {

}
