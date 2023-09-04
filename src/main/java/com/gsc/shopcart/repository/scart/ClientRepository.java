package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByApplicationId(Long applicationId);
}
