package com.javatechie.saga.order.controller;

import com.javatechie.movie.domain.Movie;
import com.javatechie.saga.order.entity.PurchaseOrder;
import com.javatechie.saga.order.service.OrderService;
import com.javatechie.saga.payment.repository.UserBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private String userId;
    @Autowired
    private OrderService orderService;


    @GetMapping("/create")
    public PurchaseOrder createOrder(){
        return orderService.createOrder();
    }

    @PostMapping("/film")
    public void createRequest(@RequestBody Movie movie){
        orderService.createOrderRequest(movie, userId);
    }

    @PostMapping("/userId")
    public void passingIdUser(@RequestBody String identifier) {
            userId = identifier;
    }

    @GetMapping
    public List<PurchaseOrder> getOrders(){
        return orderService.getAllOrders();
    }


}
