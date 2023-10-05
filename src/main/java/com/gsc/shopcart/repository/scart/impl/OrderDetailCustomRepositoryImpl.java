package com.gsc.shopcart.repository.scart.impl;

import com.gsc.shopcart.constants.ScConstants;
import com.gsc.shopcart.model.scart.entity.OrderDetail;
import com.gsc.shopcart.repository.scart.OrderDetailCustomRepository;
import com.gsc.shopcart.security.UserPrincipal;
import com.gsc.shopcart.security.UsrLogonSecurity;
import lombok.extern.log4j.Log4j;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Log4j
public class OrderDetailCustomRepositoryImpl implements OrderDetailCustomRepository {

    private final UsrLogonSecurity usrLogonSecurity;
    @PersistenceContext
    private EntityManager em;

    public OrderDetailCustomRepositoryImpl(UsrLogonSecurity usrLogonSecurity) {
        this.usrLogonSecurity = usrLogonSecurity;
    }


    @Override
    public List<OrderDetail> getOrderDetailByIdOrder(Integer idOrder, Integer idOrderDetailStatus, UserPrincipal user){
        String criteria = "1=1";

        if(idOrderDetailStatus!=-1)
                criteria+= " AND ID_ORDER_STATUS = "+idOrderDetailStatus+" ";
        if (user.getAuthorities()==null || user.getAuthorities().isEmpty())
                usrLogonSecurity.getAuthorities(user);
        if (user.getAuthorities().contains(ScConstants.PROFILE_SUPPLIER) && !user.getAuthorities().contains(ScConstants.PROFILE_TCAP))
            criteria+= " AND ID_SUPPLIER = "+user.getIdEntity()+" ";

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ORDER_DETAIL.* ").append("FROM ORDER_DETAIL ").append("WHERE ID_ORDER = ? ");
        sql.append("AND ").append(criteria).append(" ");
        sql.append("ORDER BY ID_SUPPLIER");

        Query query = em.createNativeQuery(sql.toString(), OrderDetail.class);
        query.setParameter(1,idOrder);

        return query.getResultList();
    }

}
