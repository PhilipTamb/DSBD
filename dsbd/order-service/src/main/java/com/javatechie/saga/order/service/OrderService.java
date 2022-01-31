package com.javatechie.saga.order.service;

import com.javatechie.movie.controller.MovieController;
import com.javatechie.movie.domain.Movie;
import com.javatechie.saga.commons.dto.OrderRequestDto;
import com.javatechie.saga.commons.event.OrderStatus;
import com.javatechie.saga.order.entity.PurchaseOrder;
import com.javatechie.saga.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class OrderService {

    private OrderRequestDto orderRequestDto;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStatusPublisher orderStatusPublisher;

    @Transactional
    public PurchaseOrder createOrder() {
        PurchaseOrder order = orderRepository.save(convertDtoToEntity(orderRequestDto));
        orderRequestDto.setOrderId(order.getId());
        //produce kafka event with status ORDER_CREATED
        orderStatusPublisher.publishOrderEvent(orderRequestDto, OrderStatus.ORDER_CREATED);
        return order;
    }

    public List<PurchaseOrder> getAllOrders(){
        return orderRepository.findAll();
    }


    private PurchaseOrder convertDtoToEntity(OrderRequestDto dto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setProductId(dto.getProductId());
        purchaseOrder.setUserId(dto.getUserId());
        purchaseOrder.setOrderStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(dto.getAmount());
        return purchaseOrder;
    }


    public void createOrderRequest(Movie mov, String userId) {

     //   MovieController.userID = "300";
      //  System.out.println(MovieController.userID);
        orderRequestDto = new OrderRequestDto();
        String filmID= mov.getImdbID().replace("tt","");
        orderRequestDto.setProductId(Integer.parseInt(filmID));
        orderRequestDto.setUserId((Integer.parseInt(userId)));
        try {
            if (Integer.parseInt(mov.getReleaseYear()) >= 2021) {
                orderRequestDto.setAmount(15);
            } else if (Integer.parseInt(mov.getReleaseYear()) >= 2010) {
                orderRequestDto.setAmount(10);
            } else {
                orderRequestDto.setAmount(5);
            }
        } catch (Exception except) {
            // se l'anno del film dovesse risultare non preciso (es. una serie iniziata nel 2018 e ancora non conclusa)
            // darà come anno di uscita la stringa "2018-" che non può quindi essere convertita in integer
            orderRequestDto.setAmount(5);
        }

    }
}
