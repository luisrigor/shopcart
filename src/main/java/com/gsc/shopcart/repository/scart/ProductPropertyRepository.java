package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.ProductProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductPropertyRepository extends JpaRepository<ProductProperty,Integer>, ProductPropertyCustomRepository  {

    @Query("SELECT pp FROM ProductProperty pp " +
            "WHERE pp.idProduct = :idProduct AND pp.status LIKE :status ORDER BY pp.rank")
    List<ProductProperty> findProductPropertiesByIdProductAndStatusLike(@Param("idProduct") Integer idProduct, @Param("status") Character status);

    @Query("SELECT DISTINCT pp FROM ProductProperty pp " +
            "LEFT JOIN OrderCartProductProperty ocpp ON ocpp.idProductProperty = pp.id " +
            "WHERE ocpp.idOrderCart = :idOrderCart AND pp.idProduct = :idProduct AND pp.status = 'S' " +
            "AND pp.mandatory LIKE :mandatory " +
            "ORDER BY pp.rank")
    List<ProductProperty> getDistinctProductProperty(
            @Param("idOrderCart") Integer idOrderCart,
            @Param("idProduct") Integer idProduct,
            @Param("mandatory") Character mandatory);

}