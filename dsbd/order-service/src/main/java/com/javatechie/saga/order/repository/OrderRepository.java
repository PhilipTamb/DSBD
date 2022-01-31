package com.javatechie.saga.order.repository;

import com.javatechie.saga.order.controller.OrderController;
import com.javatechie.saga.order.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<PurchaseOrder,Integer> {
 //   @Query(value = "SELECT user_id FROM user_balance WHERE user_id="+ OrderController.userId, nativeQuery = true)
  //  String checkUserId(@Param("userId") String identifier);
}
