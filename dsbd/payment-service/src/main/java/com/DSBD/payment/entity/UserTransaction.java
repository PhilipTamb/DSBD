package com.DSBD.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTransaction {

    private Integer orderId;
    private int userId;
    private int amount;

}
