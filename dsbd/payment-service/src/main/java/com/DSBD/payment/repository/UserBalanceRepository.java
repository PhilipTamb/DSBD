package com.DSBD.payment.repository;

import com.DSBD.payment.entity.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

//@Component
//@Repository
public interface UserBalanceRepository extends JpaRepository<UserBalance,Integer> {

}
