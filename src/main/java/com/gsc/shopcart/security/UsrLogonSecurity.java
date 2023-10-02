package com.gsc.shopcart.security;

import com.gsc.shopcart.constants.ScConstants;
import com.gsc.shopcart.model.usrlogon.entity.CbusUser;
import com.gsc.shopcart.model.usrlogon.entity.LexusUser;
import com.gsc.shopcart.model.usrlogon.entity.Ligacao;
import com.gsc.shopcart.model.usrlogon.entity.ToyotaUser;
import com.gsc.shopcart.repository.usrlogon.CbusUserRepository;
import com.gsc.shopcart.repository.usrlogon.LexusUserRepository;
import com.gsc.shopcart.repository.usrlogon.LigacaoRepository;
import com.gsc.shopcart.repository.usrlogon.ToyotaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.rg.dealer.Dealer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class UsrLogonSecurity {

    private final ToyotaUserRepository toyotaUserRepository;
    private final LexusUserRepository lexusUserRepository;
    private final CbusUserRepository cbusUserRepository;
    private final LigacaoRepository ligacaoRepository;


    public void setUserLogin(UserPrincipal userPrincipal) {
        if (userPrincipal.getOidNet().equalsIgnoreCase(Dealer.OID_NET_LEXUS)) {
            LexusUser userinfo = lexusUserRepository.getUserinfo("tcap1@tpo".toUpperCase());
            setUserProperties(userPrincipal, userinfo.getIdUtilizador(), userinfo.getIdEntidade(), userinfo.getOidDealer(), userinfo.getOidDealerParent(), userinfo.getNifUtilizador());
        } else if (userPrincipal.getOidNet().equalsIgnoreCase(Dealer.OID_NET_TOYOTA)) {
            ToyotaUser userinfo = toyotaUserRepository.getUserinfo("tcap1@tpo".toUpperCase());
            setUserProperties(userPrincipal, userinfo.getIdUtilizador(), userinfo.getIdEntidade(), userinfo.getOidDealer(), userinfo.getOidDealerParent(), userinfo.getNifUtilizador());
        } else {
            CbusUser userinfo = cbusUserRepository.getUserinfo("tcap1@tpo".toUpperCase());
            setUserProperties(userPrincipal, userinfo.getIdUtilizador(), userinfo.getIdEntidade(), userinfo.getOidDealer(), userinfo.getOidDealerParent(), userinfo.getNifUtilizador());
        }
    }
    private static void setUserProperties(UserPrincipal userPrincipal, Integer idUser, Integer idEntidade, String oidDealer, String oidDealerParent, String nifUtilizador) {
        userPrincipal.setIdUser(idUser);
        userPrincipal.setIdEntity(idEntidade);
        userPrincipal.setOidDealer(oidDealer);
        userPrincipal.setOidDealerParent(oidDealerParent);
        userPrincipal.setNifUtilizador(nifUtilizador);
    }

    public void getAuthorities(UserPrincipal userPrincipal) {
        if (userPrincipal.getIdUser()==null || userPrincipal.getIdUser() == -1)
            setUserLogin(userPrincipal);
        List<Ligacao> ligacaos = ligacaoRepository.getAuths(userPrincipal.getIdUser(),  Integer.valueOf(userPrincipal.getSupplierProfile()),
                Integer.valueOf(userPrincipal.getTcapProfile()), Integer.valueOf(userPrincipal.getDealerProfile()));
        setUserAuthorities(userPrincipal,userPrincipal.getSupplierProfile(),userPrincipal.getTcapProfile(),userPrincipal.getDealerProfile(),ligacaos);
    }

    private static void setUserAuthorities(UserPrincipal userPrincipal, String idProfileSupplier, String idProfileTcap,
                                          String idProfileDealer,List<Ligacao> ligacaos){
        Map<String, String> profileMapping = new HashMap<>();
        profileMapping.put(idProfileTcap, ScConstants.PROFILE_TCAP);
        profileMapping.put(idProfileSupplier, ScConstants.PROFILE_SUPPLIER);
        profileMapping.put(idProfileDealer, ScConstants.PROFILE_DEALER);
        List<String>  authorities = new ArrayList<>();
        for (Ligacao ligacao : ligacaos) {
            int idProfile = ligacao.getIdPerfil();
            String profile = profileMapping.get(String.valueOf(idProfile));
            if (profile != null && !profile.isEmpty()) {
                authorities.add(profile);
            }
        }
        userPrincipal.setAuthorities(authorities);
    }
}
