package com.gsc.shopcart.repository.scart.impl;

import com.gsc.shopcart.constants.ScConstants;
import com.gsc.shopcart.dto.GetOrderStateDTO;
import com.gsc.shopcart.exceptions.ShopCartException;
import com.gsc.shopcart.model.scart.entity.Order;
import com.gsc.shopcart.repository.scart.OrderCustomRepository;
import com.gsc.shopcart.security.UserPrincipal;
import com.rg.dealer.Dealer;
import com.sc.commons.utils.StringTasks;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Log4j
public class OrderCustomRepositoryImpl implements OrderCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Order> getOrderByCriteria(GetOrderStateDTO getOrderStateDTO, UserPrincipal userPrincipal, StringBuilder criteria, StringBuilder criteriaDetail) {

        StringBuilder sql = new StringBuilder("");
        try {
            Integer idCatalog = getOrderStateDTO.getIdCatalog();
            Integer idSupplier = getOrderStateDTO.getIdSupplier();
            Integer idUser = getOrderStateDTO.getIdUser();
            Integer orderNr = getOrderStateDTO.getOrderNr();
            String iPec = getOrderStateDTO.getIPec();
            String reference = StringTasks.cleanString(getOrderStateDTO.getReference(), StringUtils.EMPTY);
            String oidParent = StringTasks.cleanString(getOrderStateDTO.getOidParent(), StringUtils.EMPTY);
            String orderType = StringTasks.cleanString(getOrderStateDTO.getOrderType(), ScConstants.ORDER_TYPE_EXTRANET);

            if (getOrderStateDTO.getIdOrderStatus() == 1) {
                criteria.append(" AND ID NOT IN (SELECT ID_ORDER FROM ORDER_DETAIL WHERE ID_ORDER_STATUS = "
                        + ScConstants.ID_ORDER_STATUS_DELIVERED + ") ");
                criteriaDetail.append(" AND ID_ORDER_STATUS <> " + ScConstants.ID_ORDER_STATUS_CANCEL + " ");
            } else if (getOrderStateDTO.getIdOrderStatus() == 2) {
                criteria.append(" AND ID NOT IN (SELECT ID_ORDER FROM ORDER_DETAIL WHERE ID_ORDER_STATUS IN("
                        + ScConstants.ID_ORDER_STATUS_CANCEL_REQUEST + ", " + ScConstants.ID_ORDER_STATUS_PENDENT + ", "
                        + ScConstants.ID_ORDER_STATUS_RECEIVED + ")) ");
            }

           /*
            if (userPrincipal.getAuthorities().contains(ScConstants.PROFILE_SUPPLIER) && !userPrincipal.getAuthorities().contains(ScConstants.PROFILE_TCAP))
                criteriaDetail.append(" AND ID_SUPPLIER = "+userPrincipal.getIdEntity()+" ");
                */

            if(idSupplier != null && idSupplier != 0)
                criteriaDetail.append(" AND ID_SUPPLIER = "+idSupplier+" ");
            if(idUser != null && idUser != 0)
                criteria.append(" AND ID_USER = "+idUser+" ");
            if(orderNr != null && orderNr != 0)
                criteria.append(" AND ORDER_NUMBER = "+orderNr+" ");
            if(!"".equals(iPec))
                criteria.append(" AND IPEC = '"+iPec+"' ");
            if(!"".equals(reference))
                criteriaDetail.append(" AND PRODUCT_REFERENCE = '"+reference+"' ");


            sql.append("SELECT * FROM ORDER WHERE ID IN ")
                .append(" ( SELECT ID_ORDER FROM ORDER_DETAIL WHERE 1=1 ");
            if (!criteriaDetail.equals(""))
                sql.append("AND " + criteriaDetail + " ");
            sql.append(" ) AND ID_CATALOG = ? ");
            if (!criteria.equals(""))
                sql.append("AND " + criteria + " ");

            // sql.append(" AND OID_DEALER IN ("+Dealer.getHelper().getAllOidsDealers(userbean.getOidNet())+",'"+Dealer.OID_NMSC+"') ");

            if (!oidParent.equals(""))
                sql.append(" AND UPPER(OID_DEALER) IN (" + Dealer.getHelper().getActiveOidsDealersForParent(userPrincipal.getOidNet(), oidParent) + ") ");
/*
            if (userPrincipal.getAuthorities().contains(ScConstants.PROFILE_DEALER)) {
                if (orderType.equals(ScConstants.ORDER_TYPE_EXTRANET))
                    sql.append(" AND UPPER(OID_DEALER) IN (" + Dealer.getHelper().getActiveOidsDealersForParent(userPrincipal.getOidNet(), userPrincipal.getOidDealerParent()) + ") ");
                else if (orderType.equals(ScConstants.ORDER_TYPE_SITE))
                    sql.append(" AND DELIVERY_OID_DEALER IN (" + Dealer.getHelper().getActiveOidsDealersForParent(userPrincipal.getOidNet(), userPrincipal.getOidDealerParent()) + ") ");
            }

 */
            if (orderType.equals(ScConstants.ORDER_TYPE_SITE))
                sql.append(" AND (OID_DEALER='' OR OID_DEALER IS NULL) ");
            else if (orderType.equals(ScConstants.ORDER_TYPE_EXTRANET))
                sql.append(" AND OID_DEALER<>'' AND OID_DEALER IS NOT NULL ");

            sql.append(" ORDER BY DT_CREATED DESC");
            //logger.debug("sql: " + sql.toString() + " -- ID_CATALOG: " + idCatalog);

            Query query = em.createNativeQuery(sql.toString(), Order.class);

            List<Order> orderList = query
                    .setParameter(1, idCatalog)
                    .getResultList();

            return orderList;

        } catch (Exception e) {
            throw new ShopCartException("Error fetching Order By Criteria", e);
        }
    }

    @Override
    public Map<String, String> getSuppliers(Integer idProfileTcap, Integer idProfileSupplier, String oidNet) {

        try {
            StringBuilder sql = buildSuppliersQuery(idProfileTcap, idProfileSupplier, oidNet);
            log.debug("sql: " + sql);
            
            Query query = em.createNativeQuery(sql.toString());
            List<Object[]> data = query.getResultList();
            Map<String, String> mapSuppliers = new HashMap<>();

            for (Object[] currentRow: data) {
                mapSuppliers.put(currentRow[0]!=null ? (String) currentRow[0]: "", currentRow[1]!=null ? (String) currentRow[1]: "");
            }

            return mapSuppliers;

        } catch (Exception e) {
            throw new ShopCartException("Error fetching Suppliers", e);
        }
    }

    @Override
    public LinkedHashMap<String, String> getUsersByApplication(String oidNet, String oidDealerParent) {
        try {
            StringBuilder sql = buildUsersByAppQuery(oidNet, oidDealerParent);
            log.debug("sql: " + sql);

            Query query = em.createNativeQuery(sql.toString());
            List<Object[]> data = query.getResultList();
            LinkedHashMap<String, String> mapUsers = new LinkedHashMap<>();

            for (Object[] currentRow: data) {
                mapUsers.put(currentRow[0]!=null ? (String) currentRow[0]: "", currentRow[1]!=null ? (String) currentRow[1]: "");
            }
            return mapUsers;

        } catch (Exception e) {
            throw new ShopCartException("Error fetching Users By Application", e);
        }
    }

    @NotNull
    private static StringBuilder buildUsersByAppQuery(String oidNet, String oidDealerParent) {
        StringBuilder sql = new StringBuilder();
        String table;


        if (oidNet.equalsIgnoreCase(Dealer.OID_NET_TOYOTA))
            table = "TOYOTA_USERS";
        else if (oidNet.equalsIgnoreCase(Dealer.OID_NET_LEXUS))
            table = "LEXUS_USERS";
        else
            table = "CBUS_USERS";

        sql.append("SELECT ID_UTILIZADOR, NOME_UTILIZADOR FROM " + table + " ");
        if (!"".equals(oidDealerParent)) {
            sql.append("WHERE OID_DEALER_PARENT=? ");
        }
        sql.append("ORDER BY COLLATION_KEY_BIT(NOME_UTILIZADOR, 'UCA500R1_S1') ");
        return sql;
    }

    private static StringBuilder buildSuppliersQuery(Integer idProfileTcap, Integer idProfileSupplier, String oidNet) {
        StringBuilder sql = new StringBuilder();
        String table;
        if (oidNet.equalsIgnoreCase(Dealer.OID_NET_TOYOTA))
            table = "TOYOTA_USER_ENTITY_PROFILE";
        else if (oidNet.equalsIgnoreCase(Dealer.OID_NET_LEXUS))
            table = "LEXUS_USER_ENTITY_PROFILE";
        else
            table = "CBUS_USER_ENTITY_PROFILE";

        sql.append("SELECT DISTINCT(ID_ENTIDADE), DESIG_ENTIDADE FROM ").append(table).append(" WHERE ID_PERFIL IN ")
                .append(" (").append(idProfileTcap).append(",").append(idProfileSupplier).append(") ");
        return sql;
    }


}
