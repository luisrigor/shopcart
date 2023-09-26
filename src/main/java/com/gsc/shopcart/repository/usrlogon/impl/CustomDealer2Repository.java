package com.gsc.shopcart.repository.usrlogon.impl;

import com.gsc.shopcart.exceptions.SQLCustomException;
import com.rg.dealer.Dealer;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Log4j
public class CustomDealer2Repository {

    @PersistenceContext
    private EntityManager em;

    public List<Object[]> queryCustomDealer(Integer idUser, String oidNet) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT D.OID_DEALER, D.DESIG_ENTIDADE, D.END_ENTIDADE ")
                .append("FROM " + getSchemaBrandDealers(oidNet) + " D, ")
                .append("USER_ENTITY UE ").append("WHERE UE.ID_ENTITY = D.ID_ENTIDADE ")
                .append("AND D.OID_DEALER IS NOT NULL ")
                .append("AND UE.ID_USER = :idUser)");
        try {
            return em.createNativeQuery(String.valueOf(sql)).setParameter("idUser",idUser).getResultList();
        } catch (Exception ex) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("idUser", idUser);
            throw new SQLCustomException(String.valueOf(sql), parameters, ex);
        }
    }
    public static String getSchemaBrandDealers(String oidNet) {
        if (oidNet.equalsIgnoreCase(Dealer.OID_NET_TOYOTA))
            return "TOYOTA_DEALERS";
        return oidNet.equalsIgnoreCase(Dealer.OID_NET_LEXUS)?"LEXUS_DEALERS":"CBUS_DEALERS";
    }

}
