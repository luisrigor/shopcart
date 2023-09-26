package com.gsc.shopcart.repository.usrlogon;

import com.gsc.shopcart.model.usrlogon.entity.ToyotaDealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ToyotaDealerRepository extends JpaRepository<ToyotaDealer, Integer> {

    @Query(value = "SELECT D.OID_DEALER, D.DESIG_ENTIDADE, D.END_ENTIDADE  FROM TOYOTA_DEALERS D,USER_ENTITY UE WHERE UE.ID_ENTITY = D.ID_ENTIDADE " +
            "AND D.OID_DEALER IS NOT NULL AND UE.ID_USER = :idUser",nativeQuery = true)
    List<Object[]> getUserDealerWithAccess(@Param("idUser") Integer idUser);


}
