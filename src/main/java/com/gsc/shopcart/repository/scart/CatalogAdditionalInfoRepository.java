package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.CatalogAdditionalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CatalogAdditionalInfoRepository extends JpaRepository<CatalogAdditionalInfo, Integer> {

    @Query("SELECT CA FROM CatalogAdditionalInfo CA WHERE CA.idCatalog = :idCatalog ORDER BY CA.priority ")
    List<CatalogAdditionalInfo> getAdditionalInfo(@Param("idCatalog") Integer idCatalog);
}
