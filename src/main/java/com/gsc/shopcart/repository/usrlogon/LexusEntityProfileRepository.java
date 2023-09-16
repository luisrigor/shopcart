package com.gsc.shopcart.repository.usrlogon;

import com.gsc.shopcart.model.usrlogon.entity.LexusUserEntityProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LexusEntityProfileRepository extends JpaRepository<LexusUserEntityProfile,Integer> {

    @Query("SELECT DISTINCT(LU.idEntidade), LU.desigEntidade FROM LexusUserEntityProfile LU WHERE idPerfil IN (:idProfileTcap, :idProfileSupplier)")
    List<Object[]> getSuppliers(@Param("idProfileTcap") String idProfileTcap, @Param("idProfileSupplier") String idProfileSupplier);
}
