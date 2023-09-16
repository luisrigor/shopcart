package com.gsc.shopcart.repository.usrlogon;

import com.gsc.shopcart.model.usrlogon.entity.LexusUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LexusUserRepository extends JpaRepository<LexusUser, Integer>,LigacaoCustomRepository {

    @Query("SELECT LU FROM LexusUser LU WHERE UPPER(LU.loginUtilizador) = :loginUtilizador")
    LexusUser getUserinfo(@Param("loginUtilizador") String loginUtilizador);

    @Query(value = "SELECT ID_UTILIZADOR, NOME_UTILIZADOR FROM LEXUS_USERS ORDER BY COLLATION_KEY_BIT(NOME_UTILIZADOR, 'UCA500R1_S1') ",nativeQuery = true)
    List<Object[]> getIdAndName();

    @Query(value = "SELECT ID_UTILIZADOR, NOME_UTILIZADOR FROM LEXUS_USERS WHERE OID_DEALER_PARENT=:oidDealerParent ORDER BY COLLATION_KEY_BIT(NOME_UTILIZADOR, 'UCA500R1_S1') ",nativeQuery = true)
    List<Object[]> getIdAndName(@Param("oidDealerParent") String oidDealerParent);
}
