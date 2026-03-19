package com.coworking.reservationservice.kafka;

import com.coworking.reservationservice.client.MemberClient;
import com.coworking.reservationservice.dto.MemberBookingDto;
import com.coworking.reservationservice.entity.Reservation;
import com.coworking.reservationservice.entity.ReservationStatus;
import com.coworking.reservationservice.event.MemberDeletedEvent;
import com.coworking.reservationservice.event.RoomDeletedEvent;
import com.coworking.reservationservice.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationEventConsumer {

    private final ReservationRepository reservationRepository;
    private final MemberClient memberClient;
    private final ReservationEventProducer eventProducer;

    @KafkaListener(topics = "room-deleted", groupId = "reservation-service-group")
    public void onRoomDeleted(RoomDeletedEvent event) {
        log.info("Received room-deleted event: roomId={}", event.roomId());
        List<Reservation> confirmed = reservationRepository.findByRoomIdAndStatus(event.roomId(), ReservationStatus.CONFIRMED);
        for (Reservation reservation : confirmed) {
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation);

            MemberBookingDto member = memberClient.decrementBookings(reservation.getMemberId());
            boolean nowSuspended = member.getActiveBookings() >= member.getMaxConcurrentBookings();
            eventProducer.publishMemberStatus(reservation.getMemberId(), nowSuspended);

            log.info("Cancelled reservation {} due to room deletion, member {} suspension={}",
                    reservation.getId(), reservation.getMemberId(), nowSuspended);
        }
    }

    @KafkaListener(topics = "member-deleted", groupId = "reservation-service-group")
    public void onMemberDeleted(MemberDeletedEvent event) {
        log.info("Received member-deleted event: memberId={}", event.memberId());
        List<Reservation> reservations = reservationRepository.findByMemberId(event.memberId());
        reservationRepository.deleteAll(reservations);
        log.info("Deleted {} reservation(s) for member {}", reservations.size(), event.memberId());
    }
}
