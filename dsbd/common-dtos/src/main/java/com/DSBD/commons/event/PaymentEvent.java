package com.DSBD.commons.event;

import com.DSBD.commons.dto.OrderRequestDto;
import com.DSBD.commons.dto.PaymentRequestDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Data //ci crea in automatico i metodi getter e setter
public class PaymentEvent implements Event{
// analogamente a OrderEvent questo è l'oggetto che costituirà l'evento nel topic payment event e conterrà tutte le info che
    // si devono fornire a Order-service (Consumer) dopo aver effettuato il pagamento

    private UUID eventId= UUID.randomUUID();
    private Date date = new Date();
    private PaymentRequestDto paymentRequestDto;
    private PaymentStatus paymentStatus;


    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public Date getDate() {
        return date;
    }

    public PaymentEvent(PaymentRequestDto paymentRequestDto, PaymentStatus paymentStatus) {
        this.paymentRequestDto = paymentRequestDto;
        this.paymentStatus = paymentStatus;
    }
}
