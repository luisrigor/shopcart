package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.ProductProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductPropertyRepository extends JpaRepository<ProductProperty, Integer>, ProductPropertyCustomRepository {


    @Query(" DELETE FROM ProductProperty PP WHERE PP.id NOT IN (:idsProductProperties) AND PP.idProduct = :idProduct ")
    @Modifying
    void deleteProductProperties(@Param("idsProductProperties") List<Integer> idsProductProperties, @Param("idProduct") Integer idProduct);

}
