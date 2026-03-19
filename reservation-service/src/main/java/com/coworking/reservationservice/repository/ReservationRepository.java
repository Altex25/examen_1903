package com.coworking.reservationservice.repository;

import com.coworking.reservationservice.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
