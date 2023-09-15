package com.gsc.shopcart.repository.usrlogon;

import com.gsc.shopcart.model.usrlogon.entity.ToyotaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ToyotaUserRepository extends JpaRepository<ToyotaUser, Integer> {

    @Query("SELECT TU FROM ToyotaUser TU WHERE UPPER(TU.loginUtilizador) = :loginUtilizador")
    ToyotaUser getUserinfo(@Param("loginUtilizador") String loginUtilizador);
}
