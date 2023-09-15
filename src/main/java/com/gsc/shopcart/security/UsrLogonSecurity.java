package com.gsc.shopcart.security;

import com.gsc.shopcart.model.usrlogon.entity.CbusUser;
import com.gsc.shopcart.model.usrlogon.entity.LexusUser;
import com.gsc.shopcart.model.usrlogon.entity.ToyotaUser;
import com.gsc.shopcart.repository.usrlogon.CbusUserRepository;
import com.gsc.shopcart.repository.usrlogon.LexusUserRepository;
import com.gsc.shopcart.repository.usrlogon.ToyotaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.rg.dealer.Dealer;

@RequiredArgsConstructor
@Component
public class UsrLogonSecurity {

    private final ToyotaUserRepository toyotaUserRepository;
    private final LexusUserRepository lexusUserRepository;
    private final CbusUserRepository cbusUserRepository;

    public void setUserLogin(UserPrincipal userPrincipal) {
        if (userPrincipal.getOidNet().equalsIgnoreCase(Dealer.OID_NET_LEXUS)) {
            LexusUser lexusUser = lexusUserRepository.getUserinfo("tcap1@tpo".toUpperCase());
            setUserProperties(userPrincipal, lexusUser.getIdUtilizador(), lexusUser.getIdEntidade(), lexusUser.getOidDealer(), lexusUser.getOidDealerParent());
        } else if (userPrincipal.getOidNet().equalsIgnoreCase(Dealer.OID_NET_TOYOTA)) {
            ToyotaUser toyotaUser = toyotaUserRepository.getUserinfo("tcap1@tpo".toUpperCase());
            setUserProperties(userPrincipal, toyotaUser.getIdUtilizador(), toyotaUser.getIdEntidade(), toyotaUser.getOidDealer(), toyotaUser.getOidDealerParent());
        } else {
            CbusUser cbusUser = cbusUserRepository.getUserinfo("tcap1@tpo".toUpperCase());
            setUserProperties(userPrincipal, cbusUser.getIdUtilizador(), cbusUser.getIdEntidade(), cbusUser.getOidDealer(), cbusUser.getOidDealerParent());
        }
    }
    private static void setUserProperties(UserPrincipal userPrincipal, Integer idUser, Integer idEntidade, String oidDealer, String oidDealerParent) {
        userPrincipal.setIdUser(idUser);
        userPrincipal.setIdEntity(idEntidade);
        userPrincipal.setOidDealer(oidDealer);
        userPrincipal.setOidDealerParent(oidDealerParent);
    }
}
