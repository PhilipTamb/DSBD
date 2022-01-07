package com.DSBD.payment.repository;

import com.DSBD.payment.entity.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

//@Component
//@Repository
public interface UserTransactionRepository extends JpaRepository<UserTransaction, Integer> {
}
