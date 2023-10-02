package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.ProductDealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductDealerRepository extends JpaRepository<ProductDealer, Integer>, ProductDealerCustomRepository {


    @Query("DELETE ProductDealer PD WHERE  PD.idProduct = :idProduct ")
    @Modifying
    void deleteAllByIdProduct(Integer idProduct);
}
