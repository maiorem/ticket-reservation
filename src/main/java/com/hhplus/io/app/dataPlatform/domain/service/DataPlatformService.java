package com.hhplus.io.app.dataPlatform.domain.service;

import com.hhplus.io.app.reservation.domain.dto.ConfirmReservationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DataPlatformService {

    public void dataHandler(ConfirmReservationInfo reservation) {
        log.info("reservation success!! : {}", reservation);
    }

}
