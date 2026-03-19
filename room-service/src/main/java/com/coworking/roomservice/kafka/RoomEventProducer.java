package com.coworking.roomservice.kafka;

import com.coworking.roomservice.event.RoomDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomEventProducer {

    static final String ROOM_DELETED_TOPIC = "room-deleted";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishRoomDeleted(Long roomId) {
        kafkaTemplate.send(ROOM_DELETED_TOPIC, new RoomDeletedEvent(roomId));
    }
}
