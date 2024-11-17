package com.hhplus.io.app.dataPlatform.domain.event;

import com.hhplus.io.app.dataPlatform.domain.service.DataPlatformService;
import com.hhplus.io.app.reservation.domain.event.ReservationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class DataPlatformEventListener {

    private final DataPlatformService dataPlatformService;

    public DataPlatformEventListener(DataPlatformService dataPlatformService) {
        this.dataPlatformService = dataPlatformService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void reservationDataStoreEvent(ReservationEvent event) {
        dataPlatformService.dataHandler(event.info());
    }

}
