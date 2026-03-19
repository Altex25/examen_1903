package com.coworking.reservationservice.state;

import com.coworking.reservationservice.entity.Reservation;

public class CompletedState implements ReservationState {

    @Override
    public void cancel(Reservation reservation) {
        throw new IllegalStateException("Cannot cancel reservation " + reservation.getId() + ": already completed");
    }

    @Override
    public void complete(Reservation reservation) {
        throw new IllegalStateException("Reservation " + reservation.getId() + " is already completed");
    }
}
