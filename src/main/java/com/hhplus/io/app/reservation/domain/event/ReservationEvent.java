package com.hhplus.io.app.reservation.domain.event;

import com.hhplus.io.app.reservation.domain.dto.ConfirmReservationInfo;

public record ReservationEvent(ConfirmReservationInfo info, String token) {
    public static ReservationEvent create(ConfirmReservationInfo info, String token) {
        return new ReservationEvent(info, token);
    }
}
