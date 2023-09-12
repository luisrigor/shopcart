package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CatalogRepository  extends JpaRepository<Catalog, Integer>{


    @Query("SELECT C.idRootCategory FROM Catalog C WHERE C.id = :idCatalog")
    Integer getidRootCategoryByIdCatalog(@Param("idCatalog") Integer idCatalog);
}
