package com.hhplus.io.app.reservation.domain.event;

import com.hhplus.io.app.dataPlatform.domain.event.DataPlatformEventListener;
import com.hhplus.io.app.usertoken.domain.event.TokenEventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ReservationEventLisner {

    private final DataPlatformEventListener dataPlatformEventListener;
    private final TokenEventListener tokenEventListener;

    public ReservationEventLisner(DataPlatformEventListener dataPlatformEventListener, TokenEventListener tokenEventListener) {
        this.dataPlatformEventListener = dataPlatformEventListener;
        this.tokenEventListener = tokenEventListener;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void tokenHandler(ReservationEvent event){
        tokenEventListener.tokenExpired(event.token());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void dataPlatformHandler(ReservationEvent event){
        dataPlatformEventListener.reservationDataStoreEvent(event);
    }
}
