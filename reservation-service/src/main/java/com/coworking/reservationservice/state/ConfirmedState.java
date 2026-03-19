package com.coworking.reservationservice.state;

import com.coworking.reservationservice.entity.Reservation;
import com.coworking.reservationservice.entity.ReservationStatus;

public class ConfirmedState implements ReservationState {

    @Override
    public void cancel(Reservation reservation) {
        reservation.setStatus(ReservationStatus.CANCELLED);
    }

    @Override
    public void complete(Reservation reservation) {
        reservation.setStatus(ReservationStatus.COMPLETED);
    }
}
