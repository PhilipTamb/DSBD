package com.DSDB.order.repository;

import com.DSDB.order.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

//@Component
//@Repository
public interface OrderRepository extends JpaRepository<PurchaseOrder,Integer> {
} //questa implementerà in automatico i metodi CRUD in base a l'entità descritta in entity che è PurchaseOrder
