package com.DSBD.payment.config;

import com.DSBD.commons.event.OrderEvent;
import com.DSBD.commons.event.OrderStatus;
import com.DSBD.commons.event.PaymentEvent;
import com.DSBD.payment.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
public class PaymentConsumerConfig {

    @Autowired
    private PaymentService paymentService;

    //questo Payment Consumer deve ricevere dal topic order-event sul quale ha pubblicato Order Service
    //questo consente di ricevere l'evento OrderEvent, di processare i dati e di pubblicare un'altro evento PaymentEvent
    @Bean
    public Function<Flux<OrderEvent>, Flux<PaymentEvent>> paymentProcessor() {
        return orderEventFlux -> orderEventFlux.flatMap(this::processPayment);

    }

    private Mono<PaymentEvent> processPayment(OrderEvent orderEvent){
        //get the user id
        //check the balance availability
        // if balance sufficient -> Payment completed and deduct amount from DB
        //if payment fail or not sufficiet -> cancel order event end update the ammount in DB
        if(OrderStatus.ORDER_CREATED.equals(orderEvent.getOrderStatus())){
            return Mono.fromSupplier(()->this.paymentService.newOrderEvent(orderEvent));
        }else{
            return Mono.fromRunnable(()->this.paymentService.cancelOrderEvent(orderEvent));
        }
    }
}
