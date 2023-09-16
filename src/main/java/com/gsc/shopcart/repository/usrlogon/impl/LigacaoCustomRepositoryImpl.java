package com.gsc.shopcart.repository.usrlogon.impl;

import com.gsc.shopcart.constants.ScConstants;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.Product;
import com.gsc.shopcart.model.usrlogon.entity.Ligacao;
import com.gsc.shopcart.repository.usrlogon.LigacaoCustomRepository;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
public class LigacaoCustomRepositoryImpl implements LigacaoCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<String> getAuthorities(Integer idUser, String idProfileSupplier, String idProfileTcap, String idProfileDealer){
        try {

            idUser=2;

            StringBuilder sql = new StringBuilder();

            sql.append("SELECT * FROM LIGACAO WHERE ID_UTILIZADOR = ? ")
                    .append("AND ID_PERFIL IN (").append(idProfileSupplier).append(",").append(idProfileTcap).append(",").append(idProfileDealer).append(") ");
            log.debug("sql:" + sql.toString() + " idUser:" + idUser);

            Query query = em.createNativeQuery(sql.toString(), Ligacao.class);
            List<Ligacao> result = query.setParameter(1, idUser).getResultList();

            Map<String, String> profileMapping = new HashMap<>();
            profileMapping.put(idProfileTcap, ScConstants.PROFILE_TCAP);
            profileMapping.put(idProfileSupplier, ScConstants.PROFILE_SUPPLIER);
            profileMapping.put(idProfileDealer, ScConstants.PROFILE_DEALER);

            List<String>  listProfiles = new ArrayList<>();

            for (Ligacao ligacao : result) {
                int idProfile = ligacao.getIdPerfil();
                String profile = profileMapping.get(idProfile);
                if (profile != null && !profile.isEmpty()) {
                    listProfiles.add(profile);
                }
            }
            return listProfiles;

        } catch (Exception e) {
            throw new ShopCartException("Error fetching Authorities", e);
        }
    }
}
