package com.gsc.shopcart.repository.usrlogon;

import com.gsc.shopcart.model.usrlogon.entity.LexusUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LexusUserRepository extends JpaRepository<LexusUser, Integer> {

    @Query("SELECT LU FROM LexusUser LU WHERE UPPER(LU.loginUtilizador) = :loginUtilizador")
    LexusUser getUserinfo(@Param("loginUtilizador") String loginUtilizador);
}
