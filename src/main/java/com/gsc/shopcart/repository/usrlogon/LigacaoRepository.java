package com.gsc.shopcart.repository.usrlogon;

import com.gsc.shopcart.model.usrlogon.entity.Ligacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LigacaoRepository extends JpaRepository<Ligacao,Integer> {
    @Query("SELECT L FROM Ligacao L WHERE L.idUtilizador = :idUtilizador AND L.idPerfil IN (:idProfileSupplier,:idProfileTcap,:idProfileDealer)")
    List<Ligacao> getAuths(@Param("idUtilizador") Integer idUtilizador,
                  @Param("idProfileSupplier") Integer idProfileSupplier,
                  @Param("idProfileTcap") Integer idProfileTcap,
                  @Param("idProfileDealer") Integer idProfileDealer);
}
