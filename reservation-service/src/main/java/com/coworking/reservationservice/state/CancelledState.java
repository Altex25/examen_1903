package com.coworking.reservationservice.state;

import com.coworking.reservationservice.entity.Reservation;

public class CancelledState implements ReservationState {

    @Override
    public void cancel(Reservation reservation) {
        throw new IllegalStateException("Reservation " + reservation.getId() + " is already cancelled");
    }

    @Override
    public void complete(Reservation reservation) {
        throw new IllegalStateException("Cannot complete reservation " + reservation.getId() + ": already cancelled");
    }
}
