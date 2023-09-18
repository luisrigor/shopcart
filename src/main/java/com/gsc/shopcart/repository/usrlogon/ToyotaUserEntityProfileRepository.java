package com.gsc.shopcart.repository.usrlogon;

import com.gsc.shopcart.model.usrlogon.entity.ToyotaUser;
import com.gsc.shopcart.model.usrlogon.entity.ToyotaUserEntityProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ToyotaUserEntityProfileRepository extends JpaRepository<ToyotaUserEntityProfile, Integer>{

    @Query("SELECT TU FROM ToyotaUserEntityProfile TU WHERE UPPER(TU.loginUtilizador) = :loginUtilizador")
    ToyotaUser getUserinfo(@Param("loginUtilizador") String loginUtilizador);

    @Query("SELECT DISTINCT(TU.idEntidade), TU.desigEntidade FROM ToyotaUserEntityProfile TU WHERE idPerfil IN (:idProfileTcap, :idProfileSupplier)")
    List<Object[]> getSuppliers(@Param("idProfileTcap") Integer idProfileTcap, @Param("idProfileSupplier") Integer idProfileSupplier);


}
