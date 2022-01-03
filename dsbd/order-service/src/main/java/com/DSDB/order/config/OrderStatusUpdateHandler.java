package com.DSDB.order.config;

import com.DSBD.commons.dto.OrderRequestDto;
import com.DSBD.commons.event.OrderStatus;
import com.DSBD.commons.event.PaymentStatus;
import com.DSDB.order.entity.PurchaseOrder;
import com.DSDB.order.repository.OrderRepository;
import com.DSDB.order.service.OrderStatusPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
@Configuration
public class OrderStatusUpdateHandler {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderStatusPublisher publisher;

    public void updateOrder(int id, Consumer<PurchaseOrder> consumer){
        repository.findById(id).ifPresent(consumer.andThen(this::updateOrder));
    }

    private void updateOrder (PurchaseOrder purchaseOrder){
        boolean isPaymentComplete = PaymentStatus.PAYMENT_COMPLETED.equals(purchaseOrder.getPaymentStatus());
        OrderStatus orderStatus = isPaymentComplete? OrderStatus.ORDER_COMPLETED:OrderStatus.ORDER_CANCELLED;
        //È possibile utilizzare l'operatore ternario per eseguire la logica condizionale if-then-else all'interno dell'espressione. Un esempio minimo è:
        //stringa falseString = parser.parseExpression ( "false 'trueExp':? 'falseExp'" ) .getValue (. String classe );
        //In questo caso, il valore booleano false restituisce il valore di stringa 'falseExp'.
        //quindi se isPaymentComplete == TRUE allora OrderStatus.ORDER_COMPLETED
        //else OrderStatus.ORDER_CANCELLED
        purchaseOrder.setOrderStatus(orderStatus);
            if(!isPaymentComplete){ //se il pagamento non è completo
                publisher.publishOrderEvent(convertEntityToDto(purchaseOrder),orderStatus);
            }


        }

    public OrderRequestDto convertEntityToDto(PurchaseOrder purchaseOrder){
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setOrderId(purchaseOrder.getId());
        orderRequestDto.setUserId(purchaseOrder.getUserId());
        orderRequestDto.setAmount(purchaseOrder.getPrice());
        orderRequestDto.setProductId(purchaseOrder.getProductId());
        return orderRequestDto;
    }
}
