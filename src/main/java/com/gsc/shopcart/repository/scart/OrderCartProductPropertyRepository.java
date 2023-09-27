package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.OrderCartProductProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderCartProductPropertyRepository extends JpaRepository<OrderCartProductProperty,Integer> {
    @Query("SELECT DISTINCT odpp.id FROM OrderCartProductProperty odpp " +
            "LEFT JOIN ProductProperty pp ON odpp.idProductProperty = pp.id " +
            "WHERE odpp.idOrderCart = :idOrderCart AND pp.idProduct = :idProduct " +
            "ORDER BY odpp.id")
    List<Integer> getIdsOrderCartProductProperty(
            @Param("idOrderCart") Integer idOrderCart,
            @Param("idProduct") Integer idProduct);

}
