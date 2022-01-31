package com.javatechie.saga.payment.controller;


import com.javatechie.saga.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/userId")
    public boolean checkUserId(@RequestBody String utenteId) {
        return paymentService.verifyId(utenteId);
    }
}
