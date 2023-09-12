package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer>, ProductCustomRepository {
}
