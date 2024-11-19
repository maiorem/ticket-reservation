package com.hhplus.io.app.reservation.domain.event;

import com.hhplus.io.app.reservation.domain.dto.ConfirmReservationInfo;
import com.hhplus.io.common.support.utils.JsonUtils;

import java.util.UUID;

public record ReservationEvent(ConfirmReservationInfo info, String token, String aggregateId) {
    public static ReservationEvent create(ConfirmReservationInfo info, String token) {
        return new ReservationEvent(info, token, UUID.randomUUID().toString());
    }
    public static ReservationEvent convertFromString(String value){
        return JsonUtils.fromJsonString(value, ReservationEvent.class);
    }
}
