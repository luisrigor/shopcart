package com.gsc.shopcart.repository.usrlogon;

import java.util.List;

public interface LigacaoCustomRepository {
    List<String> getAuthorities(Integer idUser, String idProfileSupplier, String idProfileTcap, String idProfileDealer);
}
