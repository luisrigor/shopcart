package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Integer>, ProductCustomRepository {
    @Query("SELECT P.billTo FROM Product P WHERE P.id = :id")
    String getBillToByIdProduct(@Param("id") Integer id);
}

