package com.DSBD.payment.services;

import com.DSBD.commons.dto.OrderRequestDto;
import com.DSBD.commons.dto.PaymentRequestDto;
import com.DSBD.commons.event.OrderEvent;
import com.DSBD.commons.event.PaymentEvent;
import com.DSBD.commons.event.PaymentStatus;
import com.DSBD.payment.entity.UserBalance;
import com.DSBD.payment.entity.UserTransaction;
import com.DSBD.payment.repository.UserBalanceRepository;
import com.DSBD.payment.repository.UserTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Nodes.collect;

@Service
public class PaymentService {

    @Autowired
    private UserBalanceRepository userBalanceRepository;
    @Autowired
    private UserTransactionRepository userTransactionRepository;


    @PostConstruct //si attiva nella fase iniziale dell'applicazione
    public void initUserBalanceInDB(){
        userBalanceRepository.saveAll( Stream.of( new UserBalance(101, 5000),
                new UserBalance(102, 3000),
                new UserBalance(103, 4200),
                new UserBalance(104, 20000),
                new UserBalance(105, 999)).collect(Collectors.toList()));
    }
    /*  1) get the user id
        2) check the balance availability
        3a) if balance sufficient -> Payment completed and deduct amount from DB
        3b) if payment fail or not sufficiet -> cancel order event end update the ammount in DB */
    @Transactional// L' annotazione @Transactional sono i metadati che specificano la semantica delle transazioni su un metodo. Abbiamo due modi per eseguire il rollback di una transazione: dichiarativo e programmatico. Nell'approccio dichiarativo, annotiamo i metodi con l' annotazione @ Transactional . L' annotazione @Transactional utilizza gli attributi rollbackFor o rollbackForClassName per eseguire il rollback delle transazioni e gli attributi noRollbackFor o noRollbackForClassName per evitare il rollback sulle eccezioni elencate.Il comportamento di rollback predefinito nell'approccio dichiarativo eseguirà il rollback sulle eccezioni di runtime.
    public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
        // usiamo orderRequestDto per processare il payment, per crearlo prendiamo le info dall'OrderEvent che abbiamo ricevuto dal topic
        OrderRequestDto orderRequestDto = orderEvent.getOrderRequestDto();

        //creiamo un paymentRequestDto con associato a orderdRequestDto per processare l'ordine. passo 1) get the user id fatto
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(orderRequestDto.getOrderId(),
                orderRequestDto.getUserId(),orderRequestDto.getAmount());

        //prende un UserBalance avente un particolare UserId, ad esempio UserBalance(101)
        return userBalanceRepository.findById(orderRequestDto.getUserId())
                //adesso andiamo a controllare il campo price di questo userBalance. 2) check the balance availability
                .filter(ub->ub.getPrice()>orderRequestDto.getAmount())
                //verifica che il campo price dello UserBalance associato allo UserId sia > dell'ammontare (Amount) richiesto dall'OrderRequestDTO
                //se l'amontere richiesto è dentro il limite di quello posseduto dall'utente si può procedere con il decurtamento dell'ammont dal price dell'UserBalance
                .map(ub->{ //espressione Lambda (Lista degli argomenti) -> Espressione     oppure    (Lista degli argomenti)->{ istruzioni; }
                    ub.setPrice(ub.getPrice()-orderRequestDto.getAmount());
                    userTransactionRepository.save(new UserTransaction(orderRequestDto.getOrderId(),orderRequestDto.getUserId(),orderRequestDto.getAmount()));
                    return new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_COMPLETED);
                }).orElse(new PaymentEvent(paymentRequestDto,PaymentStatus.PAYMENT_FAILED));
    }

    @Transactional
    public void cancelOrderEvent(OrderEvent orderEvent) {

        userTransactionRepository.findById(orderEvent.getOrderRequestDto().getOrderId())
                .ifPresent(ut->{
                    userTransactionRepository.delete(ut);
                    userTransactionRepository.findById(ut.getUserId())
                            .ifPresent(ub->ub.setAmount(ub.getAmount()+ut.getAmount())); //stiamo facendo il rollback
                });
    }
}
