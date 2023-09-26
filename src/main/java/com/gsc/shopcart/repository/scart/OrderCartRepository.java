package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.OrderCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface OrderCartRepository extends JpaRepository<OrderCart, Integer>, OrderCartCustomRepository {

    List<OrderCart> findByIdUserAndIdCatalog(Integer idUser, Integer idCatalog);


}
