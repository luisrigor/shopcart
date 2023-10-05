package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.model.scart.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Integer>, OrderDetailCustomRepository {

    List<OrderDetail> findByIdOrderAndIdOrderStatus(Integer idOrder, Integer idOrderStatus);


}

