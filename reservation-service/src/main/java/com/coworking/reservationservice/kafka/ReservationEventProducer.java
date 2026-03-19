package com.coworking.reservationservice.kafka;

import com.coworking.reservationservice.event.MemberStatusEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationEventProducer {

    static final String MEMBER_STATUS_TOPIC = "member-status";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishMemberStatus(Long memberId, boolean suspended) {
        kafkaTemplate.send(MEMBER_STATUS_TOPIC, new MemberStatusEvent(memberId, suspended));
    }
}
