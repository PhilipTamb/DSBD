package com.DSDB.order.controller;

import com.DSBD.commons.dto.OrderRequestDto;
import com.DSDB.order.entity.PurchaseOrder;
import com.DSDB.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create") //  /order/create
    public PurchaseOrder createOrder(@RequestBody OrderRequestDto orderRequestDto){
        return orderService.createOrder(orderRequestDto);
    }

    @GetMapping// /order
    public List<PurchaseOrder> getOrder(){
        return orderService.getAllOrders();
    }
}
