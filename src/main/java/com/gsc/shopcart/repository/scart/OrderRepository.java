package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.Order;
import com.gsc.shopcart.model.scart.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Integer>,OrderCustomRepository {

    @Modifying
    @Transactional
    @Query("UPDATE Order O SET O.alGeneratedFileName = :alGeneratedFileName, O.dtAlGenerated = :dtAlGenerated, " +
            "O.changedBy = :changedBy, O.dtChanged = :dtChanged  WHERE O.id = :id")
    void updateAlData(@Param("alGeneratedFileName") String alGeneratedFileName, @Param("dtAlGenerated") LocalDateTime dtAlGenerated,
                        @Param("changedBy") String changedBy, @Param("dtChanged") LocalDateTime dtChanged, @Param("id") Integer id);
}
