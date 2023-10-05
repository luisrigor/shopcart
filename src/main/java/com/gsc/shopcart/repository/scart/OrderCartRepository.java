package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.OrderCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


public interface OrderCartRepository extends JpaRepository<OrderCart, Integer>, OrderCartCustomRepository {

    List<OrderCart> findByIdUserAndIdCatalog(Integer idUser, Integer idCatalog);

    @Query("SELECT oc FROM OrderCart oc WHERE oc.idUser = :idUser AND oc.idCatalog = :idCatalog " +
            "AND oc.idProduct = :idProduct " +
            "AND (oc.idProductVariant = :idProductVariant OR :idProductVariant <= 0)")
    Optional<OrderCart> getOrderCart(@Param("idUser") Integer idUser, @Param("idCatalog") Integer idCatalog,
            @Param("idProduct") Integer idProduct, @Param("idProductVariant") Integer idProductVariant);

    @Transactional
    @Modifying
    @Query("DELETE FROM OrderCart oc WHERE oc.idCatalog=:idCatalog AND oc.idUser = :idUser")
    void cleanCart(@Param("idCatalog") Integer idCatalog, @Param("idUser") Integer integer);
}
