package com.gsc.shopcart.repository.usrlogon;

import com.gsc.shopcart.model.usrlogon.entity.CbusUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CbusUserRepository extends JpaRepository<CbusUser, Integer> {

    @Query("SELECT CU FROM CbusUser CU WHERE UPPER(CU.loginUtilizador) = :loginUtilizador")
    CbusUser getUserinfo(@Param("loginUtilizador") String loginUtilizador);
}
