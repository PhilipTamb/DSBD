package com.DSBD.commons.event;

import com.DSBD.commons.dto.OrderRequestDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;
@NoArgsConstructor
@Data //ci crea in automatico i metodi getter e setter
public class OrderEvent implements Event{
//questo è l'oggetto che costituirà l'evento nel topic order event e conterrà tutte le info che
    // si devono fornire a PaymentService (Consumer) per effettuare il pagamento
    private UUID eventId = UUID.randomUUID(); /*UUID (identificatore univoco universale), noto anche come GUID
    (identificatore univoco globale) rappresenta un valore lungo 128 bit che è univoco per tutti gli scopi pratici.
     La rappresentazione standard dell'UUID utilizza cifre esadecimali (ottetti):123e4567-e89b-12d3-a456-556642440000  */
    private Date date = new Date();
    private OrderRequestDto orderRequestDto;
    private OrderStatus orderStatus;

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public Date getDate() {
        return date;
    }

    public OrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus){
        this.orderRequestDto = orderRequestDto;
        this.orderStatus = orderStatus;
    }


}
