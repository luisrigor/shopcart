package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.ProductProperty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPropertyRepository extends JpaRepository<ProductProperty, Integer>, ProductPropertyCustomRepository {
}
