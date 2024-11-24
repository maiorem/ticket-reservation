package com.hhplus.io.app.dataPlatform.domain.event;

import com.hhplus.io.app.dataPlatform.domain.service.DataPlatformService;
import com.hhplus.io.app.reservation.domain.event.ReservationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataPlatformEventListener {

    private final DataPlatformService dataPlatformService;

    public DataPlatformEventListener(DataPlatformService dataPlatformService) {
        this.dataPlatformService = dataPlatformService;
    }

    public void reservationDataStoreEvent(ReservationEvent event) {
        dataPlatformService.dataHandler(event.info());
    }

}
