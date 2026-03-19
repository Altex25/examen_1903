package com.coworking.reservationservice.service;

import com.coworking.reservationservice.client.MemberClient;
import com.coworking.reservationservice.client.RoomClient;
import com.coworking.reservationservice.dto.MemberBookingDto;
import com.coworking.reservationservice.entity.Reservation;
import com.coworking.reservationservice.entity.ReservationStatus;
import com.coworking.reservationservice.kafka.ReservationEventProducer;
import com.coworking.reservationservice.repository.ReservationRepository;
import com.coworking.reservationservice.state.ReservationState;
import com.coworking.reservationservice.state.ReservationStateFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomClient roomClient;
    private final MemberClient memberClient;
    private final ReservationEventProducer eventProducer;

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found: " + id));
    }

    public Reservation create(Reservation reservation) {
        if (!roomClient.isAvailable(reservation.getRoomId())) {
            throw new IllegalStateException("Room " + reservation.getRoomId() + " is not available");
        }
        if (memberClient.isSuspended(reservation.getMemberId())) {
            throw new IllegalStateException("Member " + reservation.getMemberId() + " is suspended");
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        Reservation saved = reservationRepository.save(reservation);

        roomClient.updateAvailability(reservation.getRoomId(), false);
        MemberBookingDto member = memberClient.incrementBookings(reservation.getMemberId());

        if (member.getActiveBookings() >= member.getMaxConcurrentBookings()) {
            eventProducer.publishMemberStatus(reservation.getMemberId(), true);
        }

        return saved;
    }

    public Reservation cancel(Long id) {
        Reservation reservation = findById(id);

        ReservationState state = ReservationStateFactory.getState(reservation.getStatus());
        state.cancel(reservation); // lève IllegalStateException si transition invalide

        Reservation saved = reservationRepository.save(reservation);

        roomClient.updateAvailability(reservation.getRoomId(), true);
        MemberBookingDto member = memberClient.decrementBookings(reservation.getMemberId());

        if (member.getActiveBookings() < member.getMaxConcurrentBookings()) {
            eventProducer.publishMemberStatus(reservation.getMemberId(), false);
        }

        return saved;
    }

    public Reservation complete(Long id) {
        Reservation reservation = findById(id);

        ReservationState state = ReservationStateFactory.getState(reservation.getStatus());
        state.complete(reservation); // lève IllegalStateException si transition invalide

        Reservation saved = reservationRepository.save(reservation);

        roomClient.updateAvailability(reservation.getRoomId(), true);
        MemberBookingDto member = memberClient.decrementBookings(reservation.getMemberId());

        if (member.getActiveBookings() < member.getMaxConcurrentBookings()) {
            eventProducer.publishMemberStatus(reservation.getMemberId(), false);
        }

        return saved;
    }
}
