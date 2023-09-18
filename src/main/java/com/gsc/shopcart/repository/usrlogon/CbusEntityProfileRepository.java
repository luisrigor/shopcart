package com.gsc.shopcart.repository.usrlogon;

import com.gsc.shopcart.model.usrlogon.entity.CbusUserEntityProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CbusEntityProfileRepository extends JpaRepository<CbusUserEntityProfile,Integer> {

    @Query("SELECT DISTINCT(CU.idEntidade), CU.desigEntidade FROM CbusUserEntityProfile CU WHERE idPerfil IN (:idProfileTcap, :idProfileSupplier)")
    List<Object[]> getSuppliers(@Param("idProfileTcap") Integer idProfileTcap, @Param("idProfileSupplier") Integer idProfileSupplier);

}
