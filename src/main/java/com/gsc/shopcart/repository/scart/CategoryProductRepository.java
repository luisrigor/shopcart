package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.CategoryProduct;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;

public interface CategoryProductRepository extends JpaRepository<CategoryProduct, Integer> {

    @Modifying
    @Transactional
    @Query("delete from CategoryProduct C where C.idProduct=:idProduct")
    void deleteCategoryProductByIdProduct(@Param("idProduct") Integer idProduct);
}
