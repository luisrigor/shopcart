package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface  CategoryRepository extends JpaRepository<Category, Integer>, CategoryCustomRepository {

    @Query("SELECT  C FROM Category  C WHERE C.idParent = :idParent AND C.id <> 0 AND C.status = 'ACTIVO' " +
            "ORDER BY C.displayOrder, C.name")
    List<Category>  getCategoriesByIdParent(@Param("idParent") Integer idParent);

    @Query("SELECT  C FROM Category  C WHERE C.idParent = :idParent AND C.id <> 0 " +
            "ORDER BY C.displayOrder, C.name")
    List<Category>  getCategoriesByIdParentBkOff(@Param("idParent") Integer idParent);
}
