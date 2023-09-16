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


            if (userPrincipal.getAuthorities().contains(ScConstants.PROFILE_SUPPLIER) && !userPrincipal.getAuthorities().contains(ScConstants.PROFILE_TCAP))
                criteriaDetail.append(" AND ID_SUPPLIER = "+userPrincipal.getIdEntity()+" ");


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

            if (!oidParent.equals(""))
                sql.append(" AND UPPER(OID_DEALER) IN (" + Dealer.getHelper().getActiveOidsDealersForParent(userPrincipal.getOidNet(), oidParent) + ") ");

            if (userPrincipal.getAuthorities().contains(ScConstants.PROFILE_DEALER)) {
                if (orderType.equals(ScConstants.ORDER_TYPE_EXTRANET))
                    sql.append(" AND UPPER(OID_DEALER) IN (" + Dealer.getHelper().getActiveOidsDealersForParent(userPrincipal.getOidNet(), userPrincipal.getOidDealerParent()) + ") ");
                else if (orderType.equals(ScConstants.ORDER_TYPE_SITE))
                    sql.append(" AND DELIVERY_OID_DEALER IN (" + Dealer.getHelper().getActiveOidsDealersForParent(userPrincipal.getOidNet(), userPrincipal.getOidDealerParent()) + ") ");
            }


            if (orderType.equals(ScConstants.ORDER_TYPE_SITE))
                sql.append(" AND (OID_DEALER='' OR OID_DEALER IS NULL) ");
            else if (orderType.equals(ScConstants.ORDER_TYPE_EXTRANET))
                sql.append(" AND OID_DEALER<>'' AND OID_DEALER IS NOT NULL ");

            sql.append(" ORDER BY DT_CREATED DESC");

            Query query = em.createNativeQuery(sql.toString(), Order.class);

            return query
                    .setParameter(1, idCatalog)
                    .getResultList();

        } catch (Exception e) {
            throw new ShopCartException("Error fetching Order By Criteria", e);
        }
    }
}
