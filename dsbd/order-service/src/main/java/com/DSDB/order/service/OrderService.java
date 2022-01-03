package com.DSDB.order.service;

import com.DSBD.commons.dto.OrderRequestDto;
import com.DSBD.commons.event.OrderStatus;
import com.DSDB.order.entity.PurchaseOrder;
import com.DSDB.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStatusPublisher orderStatusPublisher;

    @Transactional
    public PurchaseOrder createOrder(OrderRequestDto orderRequestDto) {
       PurchaseOrder order = orderRepository.save(convertDtoToEntity(orderRequestDto));
       orderRequestDto.setOrderId(order.getId());
       /*produce l'evento Kafka con status ORDER_CREATED e viene pubblicato nel topic order-event
        questo topic verrà consumato da PaymentService che a sua volta produrrà un evento payment-event
        per specificare se il pagamento sarà SUCCESS o FAILED, questo evento sarà consumato da OrderService
        */
        //qui stiamo richiamando il metodo di OrderStatusPublisher passandogli le info per poter
        // pubblicare l'evento OrderEvent
        orderStatusPublisher.publishOrderEvent(orderRequestDto,OrderStatus.ORDER_CREATED);
        return order;

    }

    public List<PurchaseOrder> getAllOrders(){
        return orderRepository.findAll();
    }

    private PurchaseOrder convertDtoToEntity(OrderRequestDto dto){
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setProductId(dto.getProductId());
        purchaseOrder.setUserId(dto.getUserId());
        purchaseOrder.setOrderStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(dto.getAmount());
        return purchaseOrder;
    }
}
