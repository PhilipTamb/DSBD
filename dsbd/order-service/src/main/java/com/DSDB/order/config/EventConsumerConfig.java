package com.DSDB.order.config;

import com.DSBD.commons.event.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class EventConsumerConfig {

    @Autowired
    private OrderStatusUpdateHandler handler;

    @Bean  //consumiamo dal topic payment-event
    public Consumer<PaymentEvent> paymentEventConsumer(){
        //listen payment-event-topic
        //will check payment status
        //if payment status completed -> complete the order
        //if payment status failed -> cancel order

        //espressione Lambda (Lista degli argomenti) -> Espressione     oppure    (Lista degli argomenti)->{ istruzioni; }
        return (payment) -> handler.updateOrder(payment.getPaymentRequestDto().getOrderId(),po->{po.setPaymentStatus((payment.getPaymentStatus()));

        });

    }
}
