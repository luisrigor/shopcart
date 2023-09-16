package com.gsc.shopcart.repository.usrlogon;

import com.gsc.shopcart.model.usrlogon.entity.Ligacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LigacaoRepository extends JpaRepository<Ligacao,Integer>,LigacaoCustomRepository {
}
