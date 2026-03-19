package com.coworking.memberservice.kafka;

import com.coworking.memberservice.event.MemberDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberEventProducer {

    static final String MEMBER_DELETED_TOPIC = "member-deleted";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishMemberDeleted(Long memberId) {
        kafkaTemplate.send(MEMBER_DELETED_TOPIC, new MemberDeletedEvent(memberId));
    }
}
