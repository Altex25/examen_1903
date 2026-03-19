package com.coworking.reservationservice.state;

import com.coworking.reservationservice.entity.ReservationStatus;

public class ReservationStateFactory {

    private ReservationStateFactory() {}

    public static ReservationState getState(ReservationStatus status) {
        return switch (status) {
            case CONFIRMED -> new ConfirmedState();
            case CANCELLED -> new CancelledState();
            case COMPLETED -> new CompletedState();
        };
    }
}
