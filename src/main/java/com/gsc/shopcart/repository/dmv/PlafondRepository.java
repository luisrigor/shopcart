package com.gsc.shopcart.repository.dmv;

import com.gsc.shopcart.model.dmv.entity.Plafond;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlafondRepository extends JpaRepository<Plafond, Integer> {

    Optional<Plafond> findByOidDealerAndYearAndType(String oidDealer, Integer year, String type);

}
